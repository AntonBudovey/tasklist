package com.anbudo.tasklist.service.impl;

import com.anbudo.tasklist.domain.MailType;
import com.anbudo.tasklist.domain.user.User;
import com.anbudo.tasklist.service.MailService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

import org.thymeleaf.context.Context;
import java.util.Properties;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    @Override
    public void sendEmail(User receiver, MailType type, Properties params) {
        switch (type) {
            case REGISTRATION -> sendRegistrationEmail(receiver, params);
            case REMINDER -> sendReminderEmail(receiver, params);
            default -> {}
        }

    }
    @SneakyThrows
    private void sendRegistrationEmail(User user, Properties params) {
        log.info("Sending registration email to " + user.getUsername());
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,
                false, "UTF-8");
        helper.setSubject("Thank you for registration, " + user.getName() + "!");
        helper.setTo(user.getUsername());
        String emailContent = getRegistrationEmailContent(user, params);
        helper.setText(emailContent, true);
        mailSender.send(mimeMessage);

    }

    @SneakyThrows
    private void sendReminderEmail(User user, Properties params) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,
                false, "UTF-8");
        helper.setSubject("You have task to do in 1 hour");
        helper.setTo(user.getUsername());
        String emailContent = getReminderEmailContent(user, params);
        helper.setText(emailContent, true);
        mailSender.send(mimeMessage);
    }

    private String getReminderEmailContent(User user, Properties params) {
        Context context = new Context();
        context.setVariable("name", user.getName());
        context.setVariable("title", params.getProperty("task.title"));
        context.setVariable("description", params.getProperty("task.description"));
        return templateEngine.process("mail/reminder", context);
    }

    private String getRegistrationEmailContent(User user, Properties params) {
        log.info("Getting registration email content for " + user.getName());
        Context context = new Context();
        context.setVariable("name", user.getName());
        return templateEngine.process("mail/register", context);
    }


}
