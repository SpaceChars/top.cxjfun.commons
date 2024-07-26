package top.cxjfun.common.datasource.nosql;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("spring.datasource.nosql")
public class NoSqlProperties {

    Redis redis;

}

@Data
@ConfigurationProperties("spring.datasource.nosql.redis")
class  Redis{
    boolean enable;
}