package top.cxjfun.common.mail;

import cn.hutool.core.util.ObjectUtil;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.Assert;

import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class MailUtil {

    private static MailConfigProperties configProperties = MailConfigProperties.beanFactory.getBean(MailConfigProperties.class);

    public static void send(String to, String subject, String context, boolean html) {
        send(to, null, null, subject, context, html);
    }

    public static void send(String to, Collection<String> bcc, Collection<String> cc, String subject, String context, boolean html) {
        send(to, bcc, cc, subject, context, html, null);
    }

    public static void send(String to, Collection<String> bcc, Collection<String> cc, String subject, String context, boolean html, Map<String, File> file) {
        Collection<String> tos = new ArrayList<>();
        tos.add(to);
        send(tos, bcc, cc, subject, context, html, file);
    }

    public static void send(Collection<String> to, Collection<String> bcc, Collection<String> cc, String subject, String context, boolean html, Map<String, File> file) {
        Assert.notEmpty(to, "to don't null");
        Assert.notNull(subject, "subject don't null");

        MimeMessage mimeMessage = MailConfigProperties.mailSender.createMimeMessage();
        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setFrom(configProperties.getFrom());
            messageHelper.setTo(to.toArray(new String[0]));

            if (ObjectUtil.isNotEmpty(bcc)) {
                messageHelper.setBcc(bcc.toArray(new String[0]));
            }

            if (ObjectUtil.isNotEmpty(cc)) {
                messageHelper.setCc(cc.toArray(new String[0]));
            }

            messageHelper.setSubject(subject);
            messageHelper.setText(context, html);

            if (ObjectUtil.isNotEmpty(file)) {
                file.keySet().stream().forEach(name -> {
                    try {
                        messageHelper.addAttachment(name, file.get(name));
                    } catch (MessagingException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
            MailConfigProperties.mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

}
