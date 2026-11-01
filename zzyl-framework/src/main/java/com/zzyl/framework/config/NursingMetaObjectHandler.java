package com.zzyl.framework.config;

import java.time.LocalDateTime;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.zzyl.common.utils.SecurityUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

/** 护理业务审计字段自动填充。 */
@Component
public class NursingMetaObjectHandler implements MetaObjectHandler
{
    private String username()
    {
        var authentication = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        return authentication == null ? "system" : SecurityUtils.getUsername();
    }
    @Override
    public void insertFill(MetaObject metaObject)
    {
        strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        strictInsertFill(metaObject, "createBy", String.class, username());
        strictInsertFill(metaObject, "updateBy", String.class, username());
    }
    @Override
    public void updateFill(MetaObject metaObject)
    {
        setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
        setFieldValByName("updateBy", username(), metaObject);
    }
}
