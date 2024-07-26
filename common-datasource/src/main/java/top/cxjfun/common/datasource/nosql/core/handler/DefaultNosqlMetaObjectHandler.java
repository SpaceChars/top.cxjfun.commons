package top.cxjfun.common.datasource.nosql.core.handler;

import top.cxjfun.common.datasource.nosql.core.NosqlMetaObject;
import top.cxjfun.common.datasource.nosql.core.NosqlMetaObjectHandler;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;

public class DefaultNosqlMetaObjectHandler implements NosqlMetaObjectHandler {


    private Object getDefaultValueByFieldType(Field field){

        if (field.getType().isAssignableFrom(Date.class)||field.getType().isAssignableFrom(java.sql.Date.class)){
            return new Date();
        }else if(field.getType().isAssignableFrom(LocalDate.class)){
            return LocalDate.now();
        }else if(field.getType().isAssignableFrom(LocalDateTime.class)){
            return LocalDateTime.now();
        }else if(field.getType().isAssignableFrom(CharSequence.class)){
            return "";
        } else if (field.getType().isAssignableFrom(Number.class)) {
            return 0;
        }else{
            return null;
        }
    }

    @Override
    public void insertFill(NosqlMetaObject metaObject) {
        Collection<Field> insertFillFields = metaObject.getTableInfo().getInsertFillFields();
        insertFillFields.forEach(field -> {
            this.strictFill(metaObject,field.getName(),this.getDefaultValueByFieldType(field));
        });
    }

    @Override
    public void updateFill(NosqlMetaObject metaObject) {
        Collection<Field> updateFillFields = metaObject.getTableInfo().getUpdateFillFields();
        updateFillFields.forEach(field -> {
            this.strictFill(metaObject,field.getName(),this.getDefaultValueByFieldType(field));
        });
    }
}
