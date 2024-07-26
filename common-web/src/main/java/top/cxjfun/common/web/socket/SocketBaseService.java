package top.cxjfun.common.web.socket;

import cn.hutool.core.util.*;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.websocket.*;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;


public abstract class SocketBaseService {


    protected String randomId;

    protected Session session;

    protected static BeanFactory beanFactory;

    /**
     * 会话池
     */
    private static ConcurrentHashMap<String, Session> sessionPool = new ConcurrentHashMap<>();

    /**
     * 解析自动装配属性
     */
    private void processAutowriteFields() {
        Field[] fields = this.getClass().getDeclaredFields();
        Arrays.stream(fields).forEach(field -> {
            Autowired annotation = field.getAnnotation(Autowired.class);
            if (ObjectUtil.isEmpty(annotation)) {
                return;
            }
            Object bean = SocketBaseService.beanFactory.getBean(field.getType());
            if (ObjectUtil.isNotEmpty(bean)) {
                try {
                    field.setAccessible(true);
                    field.set(this, bean);
                } catch (IllegalAccessException e) {
                }
            }
        });
    }


    @OnOpen
    public void onConnect(Session session) {
        this.processAutowriteFields();

        this.randomId = IdUtil.nanoId();
        this.session = session;
        sessionPool.put(randomId, session);
        this.afterConnect(session);
    }

    /**
     * 连接回调
     *
     * @param session
     */
    public abstract void afterConnect(Session session);

    /**
     * 消息
     *
     * @param message
     */
    @OnMessage
    public void onMessage(String message) {
        this.afterMessage(message);
    }

    /**
     * 接受消息回调
     *
     * @param message
     */
    public abstract void afterMessage(String message);

    /**
     * 连接错误
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        this.afterError(error);
    }

    /**
     * 连接错误回调
     *
     * @param error
     */
    public abstract void afterError(Throwable error);

    /**
     * 关闭连接
     */
    @OnClose
    public void onClose() {
        this.beforeClose();
        sessionPool.remove(randomId);
    }

    /**
     * 关联连接回调
     */
    public abstract void beforeClose();
}
