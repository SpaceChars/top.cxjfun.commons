package top.cxjfun.common.web.cron;

import cn.hutool.cron.CronUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConditionalOnProperty(prefix = "spring.task.cron", name = "enabel", havingValue = "true")
@Configuration
@EnableConfigurationProperties(CronConfigProperties.class)
public class CronAutoConfig implements InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        CronUtil.setMatchSecond(true);
        CronUtil.start();
    }
}
