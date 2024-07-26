package top.cxjfun.common.datasource.nosql.core;

import top.cxjfun.common.datasource.nosql.core.service.NosqlService;

import java.lang.reflect.ParameterizedType;

public class NosqlHelper {

    /**
     * 获取表格信息
     *
     * @param entityClass
     * @return
     */
    public static NosqlTableInfo getTableInfo(Class<?> entityClass) {
        return new NosqlTableInfo(entityClass);
    }

    /**
     * 获取类型对应class
     *
     * @param mClass 类型
     * @param <M>
     * @return
     */
    public static <M> Class<?> getTypeClass(Class<M> mClass) {
        return NosqlHelper.getTypeClass(mClass.getName());
    }


    /**
     * 获取类型对应class
     *
     * @param className class 全路径名
     * @param <M>
     * @return
     */
    public static <M> Class<?> getTypeClass(String className) {
        try {
            return ClassLoader.getSystemClassLoader().loadClass(className);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }


    /**
     * 获取类型对应class
     *
     * @param serviceClass NosqlService class
     * @return
     */
    public static Class<?> getServiceEntityClass(Class<? extends NosqlService> serviceClass) {
        ParameterizedType superclass = (ParameterizedType) serviceClass.getGenericSuperclass();

        String typeName = superclass.getActualTypeArguments()[0].getTypeName();
        return NosqlHelper.getTypeClass(typeName);
    }
}
