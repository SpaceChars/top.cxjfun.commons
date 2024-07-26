package top.cxjfun.common.datasource.nosql.core;


public interface NosqlMetaObjectHandler {

    void insertFill(NosqlMetaObject metaObject);

    void updateFill(NosqlMetaObject metaObject);

    default NosqlMetaObjectHandler strictFill(NosqlMetaObject metaObject, String fieldName, Object fieldVal) {
        metaObject.setValue(fieldName,fieldVal);
        return this;
    }
}
