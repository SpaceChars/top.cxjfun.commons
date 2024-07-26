package top.cxjfun.common.web.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties("spring.web.security")
public class WebSecurityProperties {

    /**
     * MVC 忽略路径
     */
    private String[] ignoreUrl = {};

    /**
     * token生效时间
     */
    private int tokenNotBefore = 0;

    /**
     * token失效时间
     */
    private int tokenExpires = 1000 * 60 * 60;

    /**
     * token签名key
     */
    private String tokenSignerKey;


}
