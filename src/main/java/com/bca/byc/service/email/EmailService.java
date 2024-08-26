package com.bca.byc.service.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${mail.from}")
    private String fromEmail;

    public void sendOtpMessage(String identity, String to, String name, String otp) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());

        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("otp", otp);

        String template = "emails/otp";
        String subject = "Registration OTP";

        if (Objects.equals(identity, "resend")) {
            subject = "Resend OTP";
            template = "emails/otp-resend";
        } else if (Objects.equals(identity, "reset")) {
            subject = "Forgot Password OTP";
            template = "emails/otp-reset";
        }
        String html = templateEngine.process(template, context);

        helper.setTo(to);
        helper.setText(html, true);
        helper.setSubject(subject);
        helper.setFrom(fromEmail);

        javaMailSender.send(message);
    }

    public void sendOtpMessageForgotPassword(String to, String subject, String otp) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());

        Context context = new Context();
        context.setVariable("otp", otp);
        String html = templateEngine.process("emails/otp", context);

        helper.setTo(to);
        helper.setText(html, true);
        helper.setSubject(subject);
        helper.setFrom(fromEmail);

        javaMailSender.send(message);
    }


    public void sendWaitingApproval(String to, String subject, String name) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());

        Context context = new Context();
        context.setVariable("name", name);
        String html = templateEngine.process("emails/waiting-approval", context);

        helper.setTo(to);
        helper.setText(html, true);
        helper.setSubject(subject);
        helper.setFrom(fromEmail);

        javaMailSender.send(message);
    }


}
