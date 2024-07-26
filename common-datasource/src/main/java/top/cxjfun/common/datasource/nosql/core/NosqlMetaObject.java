package top.cxjfun.common.datasource.nosql.core;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.digest.MD5;
import org.apache.ibatis.reflection.MetaObject;
import org.bouncycastle.crypto.digests.MD5Digest;
import top.cxjfun.common.datasource.nosql.core.enums.PrimaryType;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.time.ZoneOffset;
import java.util.*;

public class NosqlMetaObject {

    private final Object originalObject;

    private final NosqlTableInfo tableInfo;


    public NosqlMetaObject(Object originalObject) {
        this.originalObject = originalObject;
        this.tableInfo = NosqlHelper.getTableInfo(originalObject.getClass());
    }

    /**
     * 根据Field 对象 设置字段值
     * @param field
     * @param fieldVal
     */
    private void setValue(Field field, Object fieldVal) {
        field.setAccessible(true);
        try {
            field.set(originalObject, fieldVal);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据字段名称设置字段值
     * @param fieldName
     * @param fieldVal
     */
    public void setValue(String fieldName, Object fieldVal) {
        Class<?> objectClass = originalObject.getClass();
        try {
            Field field = objectClass.getDeclaredField(fieldName);
            this.setValue(field, fieldVal);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取字段值
     * @param field
     * @return
     */
    public Object getValue(Field field) throws IllegalAccessException {
        field.setAccessible(true);
        return field.get(this.originalObject);
    }

    /**
     * 构建主键
     * @return
     */
    private String generatePrimary() {
        PrimaryType primaryType = this.tableInfo.getPrimaryType();
        String tableName = this.tableInfo.getTableName();
        String tableFieldName = this.tableInfo.getTableFieldName(this.tableInfo.getPrimaryField());

        if (primaryType == PrimaryType.DEFAULT) {
            //默认id
            return IdUtil.objectId();
        } else if (primaryType == PrimaryType.UUID) {
            //uuid
            return IdUtil.simpleUUID();
        } else {
            //自增id
            Snowflake snowflake = this.tableInfo.getSnowflake();
            if (snowflake == null) {
                snowflake = IdUtil.getSnowflake(tableName.getBytes(StandardCharsets.UTF_8).hashCode(), tableFieldName.getBytes(StandardCharsets.UTF_8).hashCode());
                this.tableInfo.setSnowflake(snowflake);
            }
            return snowflake.nextIdStr();
        }
    }

    private boolean isInsert(){
        return ObjectUtil.isEmpty(this.getPrimaryValue());
    }

    /***
     * 填充数据
     * @param metaObjectHandler
     */
    public void fillRow(NosqlMetaObjectHandler metaObjectHandler) {

        if (this.isInsert()) {
            //新增、填充id
            Field primaryField = this.tableInfo.getPrimaryField();
            String primaryValue = this.generatePrimary();
            this.setValue(primaryField, primaryValue);
            //执行用户默认handler
            metaObjectHandler.insertFill(this);
        } else {
            metaObjectHandler.updateFill(this);
        }
    }

    /**
     * 转化为表格行数据
     *
     * @return
     */
    public Map<String, Object> transformToTableRow() {
        HashMap<String, Object> result = new HashMap<>();

        Map<Field, String> names = this.getTableInfo().getFieldsNames();
        names.keySet().stream().forEach(field -> {
            try {
                field.setAccessible(true);
                result.put(names.get(field), field.get(this.originalObject));
            } catch (IllegalAccessException e) {
                result.put(names.get(field), null);
            }
        });
        return result;
    }

    /**
     * 获取组件
     * @return
     */
    public Object getPrimaryValue(){
        try {
            return this.getValue(this.getTableInfo().getPrimaryField());
        } catch (IllegalAccessException e) {
            return null;
        }
    }


    public Object getOriginalObject() {
        return originalObject;
    }

    public NosqlTableInfo getTableInfo() {
        return tableInfo;
    }
}
