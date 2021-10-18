package com.alex.cryptoBackend.service.impl;

import com.alex.cryptoBackend.dto.UserConfirmationInfo;
import com.alex.cryptoBackend.model.ConfirmationToken;
import com.alex.cryptoBackend.model.User;
import com.alex.cryptoBackend.service.EmailService;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender emailSender;
    private final Configuration configuration;

    @Value("${spring.mail.username}")
    private String from;

    @Value("${email.verification.link}")
    private String link;

    @Override
    @Async
    @SneakyThrows
    public void send(ConfirmationToken token) {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        helper.setSubject("Welcome to mycrypto!");
        helper.setFrom(from);
        User user = token.getUser();
        UserConfirmationInfo userInfo = UserConfirmationInfo.builder()
                .fullName(user.getFirstName() + " " + user.getLastName())
                .token(token.getToken())
                .build();
        helper.setTo(user.getEmail());
        final String emailContent = getEmailContent(userInfo);
        helper.setText(emailContent, true);
        emailSender.send(mimeMessage);
    }

    private String getEmailContent(UserConfirmationInfo user) throws IOException, TemplateException {
        StringWriter stringWriter = new StringWriter();
        Map<String, Object> model = new HashMap<>();
        user.setToken(link + user.getToken());
        model.put("user", user);
        configuration.getTemplate("confirm_email.ftlh").process(model, stringWriter);
        return stringWriter.getBuffer().toString();
    }
}
