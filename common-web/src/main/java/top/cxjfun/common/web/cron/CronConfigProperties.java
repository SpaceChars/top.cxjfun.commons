package top.cxjfun.common.web.cron;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("spring.task.cron")
public class CronConfigProperties {

    //是否开启线程池
    private boolean enabel;
}
