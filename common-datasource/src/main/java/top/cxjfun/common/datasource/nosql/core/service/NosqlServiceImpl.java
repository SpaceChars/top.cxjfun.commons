package top.cxjfun.common.datasource.nosql.core.service;

import cn.hutool.core.util.ObjectUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import top.cxjfun.common.datasource.nosql.core.NosqlHelper;
import top.cxjfun.common.datasource.nosql.core.NosqlMetaObject;
import top.cxjfun.common.datasource.nosql.core.NosqlMetaObjectHandler;
import top.cxjfun.common.datasource.nosql.core.NosqlTableInfo;
import top.cxjfun.common.datasource.nosql.core.handler.DefaultNosqlMetaObjectHandler;

import java.lang.reflect.ParameterizedType;

public abstract class NosqlServiceImpl<T> implements NosqlService<T> , BeanFactoryAware {

    private BeanFactory beanFactory;

    private NosqlMetaObjectHandler metaObjectHandler;

    protected Class<T> entityClass=currentEntityClass();

    protected NosqlTableInfo tableInfo=this.currentTableInfo();


    /**
     * 获取 MetaObject 处理器
     * @return
     */
    public NosqlMetaObjectHandler getMetaObjectHandler() {
        //已存在，直接返回
        if (ObjectUtil.isNotEmpty(this.metaObjectHandler)) {
            return this.metaObjectHandler;
        }

        //如果获取NosqlMetaObjectHandler失败、则赋值默认handler
        try {
            this.metaObjectHandler= beanFactory.getBean(NosqlMetaObjectHandler.class);
        }catch (BeansException exception){
            this.metaObjectHandler=new DefaultNosqlMetaObjectHandler();
        }

        return metaObjectHandler;
    }


    /**
     * 设置BeanFactory
     */
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory=beanFactory;
    }

    /**
     * service entity class
     * @return
     */
    private Class<T> currentEntityClass(){
        return (Class<T>) NosqlHelper.getServiceEntityClass(this.getClass());
    }

    /**
     * 初始化 实体类 表格信息
     * @return
     */
    private NosqlTableInfo currentTableInfo(){
        return NosqlHelper.getTableInfo(this.currentEntityClass());
    }

    /**
     * 获取BeanFactory
     * @return
     */
    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    public NosqlTableInfo getTableInfo() {
        return tableInfo;
    }

    public NosqlMetaObject getMetaObject(T entity){
        return new NosqlMetaObject(entity);
    }

}
