package top.cxjfun.common.datasource.sql;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

public class SqlServieListener implements ApplicationListener<ContextRefreshedEvent> {

    private ApplicationContext applicationContext;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        applicationContext = event.getApplicationContext();
        initTablesData();
    }

    public void initTablesData() {
        Map<String, IService> beans = applicationContext.getBeansOfType(IService.class);
        Collection<IService> services = beans.values();
        services.forEach(service -> {
            Class<? extends IService> serviceClass = service.getClass();
            Method[] methods = serviceClass.getMethods();
            Arrays.stream(methods)
                    .filter(method -> method.getName().equals("init"))
                    .findFirst()
                    .ifPresent(method -> {
                        try {
                            Class[] parameters = method.getParameterTypes();
                            //获取方法参数
                            IService[] invaokeParameters = new IService[parameters.length];
                            for (int i = 0; i < parameters.length; i++) {
                                Class<?> type = parameters[i];
                                if (IService.class.isAssignableFrom(type)) {
                                    invaokeParameters[i] = services.stream().filter(service2 -> type.isAssignableFrom(service2.getClass())).findFirst().get();
                                } else {
                                    invaokeParameters[i] = null;
                                }
                            }

                            method.invoke(service, invaokeParameters);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });
        });
    }
}
