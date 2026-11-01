package com.zzyl.framework.config;

import java.util.List;
import javax.sql.DataSource;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInterceptor;
import com.zzyl.common.core.domain.BaseEntity;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.JdbcTemplate;
import static org.junit.jupiter.api.Assertions.*;

class MybatisPlusConfigTest
{
    private AnnotationConfigApplicationContext context;
    private SqlSessionFactory factory;

    @BeforeEach
    void setUp()
    {
        context = new AnnotationConfigApplicationContext();
        // 使用应用的真实 MP / PageHelper 配置，但只启动持久层，不连接业务数据库和 Redis。
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        yaml.setResources(new FileSystemResource("../zzyl-admin/src/main/resources/application.yml"));
        context.getEnvironment().getPropertySources().addFirst(
                new PropertiesPropertySource("application", yaml.getObject()));
        context.registerBean(DataSource.class, () -> {
            JdbcDataSource dataSource = new JdbcDataSource();
            dataSource.setURL("jdbc:h2:mem:mp_config_test;MODE=MySQL;DB_CLOSE_DELAY=-1");
            return dataSource;
        });
        context.register(TestConfig.class);
        context.refresh();
        factory = context.getBean(SqlSessionFactory.class);
        factory.getConfiguration().addMapper(ProbeMapper.class);
        JdbcTemplate jdbc = new JdbcTemplate(context.getBean(DataSource.class));
        jdbc.execute("DROP TABLE IF EXISTS mp_probe");
        jdbc.execute("CREATE TABLE mp_probe (id BIGINT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(50), "
                + "create_by VARCHAR(50), update_by VARCHAR(50), create_time TIMESTAMP, "
                + "update_time TIMESTAMP, remark VARCHAR(255))");
        jdbc.update("INSERT INTO mp_probe (name) VALUES ('a'), ('b'), ('c')");
    }

    @AfterEach
    void tearDown()
    {
        PageHelper.clearPage();
        if (context != null)
        {
            context.close();
        }
    }

    @Test
    void loadsExistingXmlAndBothPaginationPlugins()
    {
        assertEquals(1, context.getBeansOfType(SqlSessionFactory.class).size());
        assertTrue(factory.getConfiguration().hasStatement("com.zzyl.system.mapper.SysUserMapper.selectUserById"));
        assertEquals(1, factory.getConfiguration().getInterceptors().stream()
                .filter(PageInterceptor.class::isInstance).count());
        MybatisPlusInterceptor mp = context.getBean(MybatisPlusInterceptor.class);
        assertTrue(factory.getConfiguration().getInterceptors().contains(mp));
        assertInstanceOf(PaginationInnerInterceptor.class, mp.getInterceptors().getLast());
    }

    @Test
    void supportsBaseMapperAndBothPaginationStyles()
    {
        try (SqlSession session = factory.openSession())
        {
            ProbeMapper mapper = session.getMapper(ProbeMapper.class);
            // SELECT 实际执行成功，验证 BaseEntity 的非数据库字段已排除。
            assertEquals(3, mapper.selectList(null).size());
            Page<ProbeEntity> mpPage = mapper.selectPage(new Page<>(2, 2), null);
            assertEquals(3, mpPage.getTotal());
            assertEquals(1, mpPage.getRecords().size());

            try (com.github.pagehelper.Page<ProbeEntity> page = PageHelper.startPage(2, 2, "id desc"))
            {
                List<ProbeEntity> rows = mapper.selectAll();
                assertEquals(3, page.getTotal());
                assertEquals(1, rows.size());
                assertEquals("a", rows.getFirst().getName());
            }
            ProbeEntity entity = new ProbeEntity();
            entity.setName("inserted");
            assertEquals(1, mapper.insert(entity));
            assertNotNull(entity.getId());
        }
    }

    @Test
    void blocksDeleteWithoutWhereClause()
    {
        try (SqlSession session = factory.openSession())
        {
            ProbeMapper mapper = session.getMapper(ProbeMapper.class);
            RuntimeException error = assertThrows(RuntimeException.class,
                    () -> mapper.deleteByMap(java.util.Map.of()));
            assertTrue(error.getMessage().contains("Prohibition of full table deletion"));
            assertEquals(3, mapper.selectCount(null));
        }
    }

    @Test
    void fillsNursingAuditFieldsOnInsertAndUpdate()
    {
        JdbcTemplate jdbc = new JdbcTemplate(context.getBean(DataSource.class));
        jdbc.execute("DROP TABLE IF EXISTS audit_probe");
        jdbc.execute("CREATE TABLE audit_probe (id BIGINT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(50), "
                + "create_by VARCHAR(50), update_by VARCHAR(50), create_time TIMESTAMP, "
                + "update_time TIMESTAMP, remark VARCHAR(255))");
        factory.getConfiguration().addMapper(AuditMapper.class);
        try (SqlSession session = factory.openSession())
        {
            AuditMapper mapper = session.getMapper(AuditMapper.class);
            AuditEntity entity = new AuditEntity();
            entity.setName("audit");
            assertEquals(1, mapper.insert(entity));
            assertNotNull(entity.getId());
            assertNotNull(entity.getCreateTime());
            assertEquals("system", entity.getCreateBy());
            var created = entity.getCreateTime();
            entity.setUpdateTime(java.time.LocalDateTime.of(2000, 1, 1, 0, 0));
            entity.setName("updated");
            assertEquals(1, mapper.updateById(entity));
            assertTrue(entity.getUpdateTime().getYear() > 2000);
            assertEquals(created, entity.getCreateTime());
            assertEquals("updated", mapper.selectById(entity.getId()).getName());
        }
    }

    interface AuditMapper extends BaseMapper<AuditEntity> { }

    @TableName("audit_probe")
    public static class AuditEntity extends com.zzyl.common.core.domain.NursingEntity
    {
        private Long id;
        private String name;
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }

    @Configuration(proxyBeanMethods = false)
    @Import({MybatisPlusAutoConfiguration.class, MybatisPlusConfig.class, NursingMetaObjectHandler.class, com.github.pagehelper.autoconfigure.PageHelperAutoConfiguration.class})
    static class TestConfig
    {
    }

    interface ProbeMapper extends BaseMapper<ProbeEntity>
    {
        @Select("SELECT * FROM mp_probe ORDER BY id")
        List<ProbeEntity> selectAll();
    }

    @TableName("mp_probe")
    public static class ProbeEntity extends BaseEntity
    {
        private Long id;
        private String name;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }
}
