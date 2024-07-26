package top.cxjfun.common.datasource.sql;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;

public class FiledsFillMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {

        this.strictInsertFill(metaObject,"createTime", LocalDateTime.class,LocalDateTime.now());
        this.strictInsertFill(metaObject,"createTime", java.util.Date.class,new java.util.Date(DateUtil.current()));
        this.strictInsertFill(metaObject,"createTime", java.sql.Date.class,new java.sql.Date(DateUtil.current()));

        this.strictInsertFill(metaObject,"updateTime", LocalDateTime.class,LocalDateTime.now());
        this.strictInsertFill(metaObject,"updateTime", java.util.Date.class,new java.util.Date(DateUtil.current()));
        this.strictInsertFill(metaObject,"updateTime", java.sql.Date.class,new java.sql.Date(DateUtil.current()));

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject,"updateTime", LocalDateTime.class,LocalDateTime.now());
        this.strictUpdateFill(metaObject,"updateTime", java.util.Date.class,new java.util.Date(DateUtil.current()));
        this.strictUpdateFill(metaObject,"updateTime", java.sql.Date.class,new java.sql.Date(DateUtil.current()));
    }
}
