package top.cxjfun.common.mail;

import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Data
public class MailConfigProperties implements BeanFactoryAware {

    public static BeanFactory beanFactory;

    public static JavaMailSenderImpl mailSender;

    @Value("${spring.mail.from}")
    private String from;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        MailConfigProperties.beanFactory=beanFactory;
        MailConfigProperties.mailSender=beanFactory.getBean(JavaMailSenderImpl.class);
    }
}
