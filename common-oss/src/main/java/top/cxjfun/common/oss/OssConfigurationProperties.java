package top.cxjfun.common.oss;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "oss")
public class OssConfigurationProperties {


    /**
     * 连接url
     */
    private String endpoint;
    /**
     *  Service Account——accesskey
     */
    private String accessKey;
    /**
     * Service Account——secretKey
     */
    private String secretKey;

    /**
     * 桶
     */
    private String bucket;

    /**
     * 分片大小
     */
    private long partSize=5 * 1024 * 1024;

}
