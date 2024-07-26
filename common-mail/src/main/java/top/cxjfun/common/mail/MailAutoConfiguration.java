package top.cxjfun.common.mail;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class MailAutoConfiguration {

    @Bean
    public MailConfigProperties mailConfigProperties(){
        return  new MailConfigProperties();
    }
}
