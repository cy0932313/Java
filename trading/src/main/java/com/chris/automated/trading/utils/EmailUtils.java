package com.chris.automated.trading.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: Chris.Y
 * @create: 2019-05-25 16:59
 **/
@Component
public class EmailUtils {
    @Autowired
    private JavaMailSender mailSender; //框架自带的

    @Value("${spring.mail.username}")  //发送人的邮箱
    private String from;

    public void sendMail(String title, String text)
    {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from); // 发送人的邮箱
        message.setSubject(title); //标题
        message.setTo("1562847880@qq.com");//发给谁  对方邮箱
        message.setText(text); //内容
        mailSender.send(message); //发送
    }
}
