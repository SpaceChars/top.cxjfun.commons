package top.cxjfun.common.web.socket;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@ConditionalOnProperty(prefix = "spring.socket", name = "enabel", havingValue = "true")
@Configuration
@EnableConfigurationProperties(WebSocketConfigProperties.class)
public class WebSocketAutoConfig implements BeanFactoryAware {

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        SocketBaseService.beanFactory=beanFactory;
    }
}
