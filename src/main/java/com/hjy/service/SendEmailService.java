package com.hjy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.concurrent.Future;


@Service
public class SendEmailService {
    @Autowired
    private JavaMailSenderImpl mailSender;
    @Value("${spring.mail.username}")
    private String from;
    @Async
    public Future<Boolean> sendSimpleEmail(String to, String subject, String content) throws  Exception
    {
        MimeMessage message = mailSender.createMimeMessage();

        try
        {
            MimeMessageHelper helper = new MimeMessageHelper(message,true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content,true);
            mailSender.send(message);
            return new AsyncResult<Boolean>(Boolean.TRUE);
        }catch (MessagingException e)
        {
            e.printStackTrace();
            return new AsyncResult<Boolean>(Boolean.FALSE);
        }

    }
}
