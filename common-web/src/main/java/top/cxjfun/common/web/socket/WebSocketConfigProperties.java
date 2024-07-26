package top.cxjfun.common.web.socket;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("spring.socket")
public class WebSocketConfigProperties {

    //是否开启WebSocket
    private boolean enabel;
}
