package top.cxjfun.common.datasource.nosql.core;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.util.Assert;
import top.cxjfun.common.datasource.nosql.core.annotation.NosqlTable;
import top.cxjfun.common.datasource.nosql.core.annotation.NosqlTableField;
import top.cxjfun.common.datasource.nosql.core.annotation.NosqlTablePrimary;
import top.cxjfun.common.datasource.nosql.core.enums.FieldFill;
import top.cxjfun.common.datasource.nosql.core.enums.PrimaryType;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class NosqlTableInfo {

    private Class<?> entityClass;

    /**
     * 表名
     */
    private String tableName;

    /**
     * 主键
     */
    private Field primaryField;

    /**
     * 组件生成策略
     */
    private PrimaryType primaryType;

    /**
     * 默认时填充字段
     */
    private Collection<Field> defaultFillFields=new ArrayList<>();

    /**
     * 新增时填充字段
     */
    private Collection<Field> insertFillFields=new ArrayList<>();

    /**
     * 更新时填充字段
     */
    private Collection<Field> updateFillFields=new ArrayList<>();

    private Map<Field, String> fieldsNameMap = new HashMap<>();

    private Snowflake snowflake;

    public NosqlTableInfo(Class<?> entityClass) {
        this.entityClass = entityClass;
        this.parseTableHeaderInfo();
        this.parseEntityFields();
    }

    /**
     * 解析表基础信息
     */
    private void parseTableHeaderInfo() {

        NosqlTable classAnnotation = entityClass.getAnnotation(NosqlTable.class);
        Assert.notNull(classAnnotation, "The 'NosqlTable' Annotation is required" );

        if (ObjectUtil.isEmpty(classAnnotation.name())) {
            this.tableName = StrUtil.toUnderlineCase(entityClass.getSimpleName());
        } else {
            this.tableName = classAnnotation.name();
        }
    }

    /**
     * 解析表格字段
     */
    private void parseEntityFields() {
        Field[] fields = this.entityClass.getDeclaredFields();
        for (Field field : fields) {
            String fieldName = StrUtil.toUnderlineCase(field.getName());

            NosqlTablePrimary primaryAnnotation = field.getAnnotation(NosqlTablePrimary.class);
            NosqlTableField fieldAnnotation = field.getAnnotation(NosqlTableField.class);

            if (primaryAnnotation != null) {
                Assert.isNull(primaryField, "The primary field is unique" );

                this.primaryField=field;
                this.primaryType=primaryAnnotation.type();

                String value = primaryAnnotation.value();
                if (value != null) {
                    fieldName = value;
                }
            }else if(fieldAnnotation!=null){

                FieldFill fieldFill = fieldAnnotation.fill();
                if (fieldFill!=null) {
                    if (fieldFill == FieldFill.INSERT) {
                        insertFillFields.add(field);
                    } else if ( fieldFill == FieldFill.UPDATE) {
                        updateFillFields.add(field);
                    } else if (fieldFill == FieldFill.INSERT_UPDATE) {
                        insertFillFields.add(field);
                        updateFillFields.add(field);
                    }else{
                        defaultFillFields.add(field);
                    }
                }

                String value = fieldAnnotation.value();
                if (value != null) {
                    fieldName = value;
                }
            }else{
                defaultFillFields.add(field);
            }



            fieldsNameMap.put(field, fieldName);
        }

        Assert.notNull(primaryField, "The primary field is required" );

    }

    public Class<?> getEntityClass() {
        return entityClass;
    }

    public String getTableName() {
        return this.tableName;
    }

    public Field getPrimaryField() {
        return primaryField;
    }

    public PrimaryType getPrimaryType() {
        return primaryType;
    }

    public Collection<Field> getInsertFillFields() {
        return insertFillFields;
    }

    public Collection<Field> getUpdateFillFields() {
        return updateFillFields;
    }

    public String getTableFieldName(Field field){
        return this.fieldsNameMap.get(field);
    }

    protected Map<Field,String> getFieldsNames(){
        return this.fieldsNameMap;
    }

    public Snowflake getSnowflake() {
        return snowflake;
    }

    public void setSnowflake(Snowflake snowflake) {
        this.snowflake = snowflake;
    }
}
