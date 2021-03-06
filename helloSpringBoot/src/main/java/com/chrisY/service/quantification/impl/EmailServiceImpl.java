package com.chrisY.service.quantification.impl;

import com.chrisY.service.quantification.IEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * @author ：ChrisY
 * @date ：Created in 2019-03-08 17:41
 * @description：
 */

@Service
public class EmailServiceImpl implements IEmailService {
    @Autowired
    private JavaMailSender mailSender; //框架自带的

    @Value("${spring.mail.username}")  //发送人的邮箱
    private String from;

    public void sendMail(String title, String text)
    {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from); // 发送人的邮箱
        message.setSubject(title); //标题
        message.setTo("cy0932313@163.com"); //发给谁  对方邮箱
        message.setText(text); //内容
        mailSender.send(message); //发送
    }
}
