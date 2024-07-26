package top.cxjfun.common.web.thread;

import lombok.Data;
import org.springframework.boot.autoconfigure.task.TaskExecutionProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("spring.task.execution")
public class TaskExecutionConfigProperties {

    //是否开启线程池
    private boolean enabel;
}
