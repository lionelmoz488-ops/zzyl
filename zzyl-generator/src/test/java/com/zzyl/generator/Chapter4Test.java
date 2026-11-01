package com.zzyl.generator;

import com.zzyl.generator.domain.GenTable;
import com.zzyl.generator.domain.GenTableColumn;
import com.zzyl.generator.util.GenUtils;
import com.zzyl.generator.util.VelocityInitializer;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.junit.jupiter.api.Test;
import java.io.StringWriter;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class Chapter4Test
{
    @Test
    void mapsDatabaseTypesForModernMySql()
    {
        for (var expected : Map.of("tinyint", "Integer", "int", "Integer", "bigint", "Long",
                "decimal(10,2)", "BigDecimal", "datetime", "LocalDateTime", "timestamp", "LocalDateTime").entrySet())
        {
            GenTable table = new GenTable();
            GenTableColumn column = new GenTableColumn();
            column.setColumnType(expected.getKey());
            column.setColumnName("sample");
            GenUtils.initColumnField(column, table);
            assertEquals(expected.getValue(), column.getJavaType());
        }
    }
    @Test
    void rendersVelocityGettingStartedExamples()
    {
        VelocityInitializer.initVelocity();
        for (String name : new String[]{"同学", "女孩", "朋友"})
        {
            VelocityContext context = new VelocityContext();
            context.put("name", name);
            StringWriter result = new StringWriter();
            Velocity.getTemplate("index.html.vm", "UTF-8").merge(context, result);
            assertTrue(result.toString().contains("加油" + name + "！"));
        }
    }
}
