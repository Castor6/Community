package com.castor6.community.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @author castor6
 * @version v1.0
 * @create 2022-03-25 21:24
 * @Description 发送邮件的类，因为需要自动注入JavaMailSender对象，所以将其作为组件注册到容器中，其次还可以方便随地自动注入调用
 */
@Slf4j
@Component
public class MailClient {
    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;    //服务端发送邮件的邮箱

    /**
     * @Author Castor6
     * @Description
     * @Date 2022/3/25 21:39
     * @Param to：目标邮箱, subject：邮件标题, content：邮件内容
     * @return void
     */
    public void sendMail(String to, String subject, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(helper.getMimeMessage());
        } catch (MessagingException e) {
            log.error("发送邮件失败:" + e.getMessage());
        }
    }
}
