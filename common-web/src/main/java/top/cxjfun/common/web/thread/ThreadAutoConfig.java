package top.cxjfun.common.web.thread;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.task.TaskExecutionProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@ConditionalOnProperty(prefix = "spring.task.execution",name = "enabel",havingValue = "true")
@Configuration
@EnableAsync
@EnableConfigurationProperties(TaskExecutionConfigProperties.class)
public class ThreadAutoConfig {

    @Bean
    public Executor customServiceExecutor(TaskExecutionProperties properties) {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();

        taskExecutor.setCorePoolSize(properties.getPool().getCoreSize());
        taskExecutor.setMaxPoolSize(properties.getPool().getMaxSize());
        taskExecutor.setQueueCapacity(properties.getPool().getQueueCapacity());
        taskExecutor.setThreadNamePrefix(properties.getThreadNamePrefix());

        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        taskExecutor.initialize();
        return taskExecutor;
    }

}
