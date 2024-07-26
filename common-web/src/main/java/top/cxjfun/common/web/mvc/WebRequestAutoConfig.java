package top.cxjfun.common.web.mvc;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class WebRequestAutoConfig {

    @Bean
    public RequestControllerAdvice requestControllerAdvice(){
        return new RequestControllerAdvice();
    }
}
