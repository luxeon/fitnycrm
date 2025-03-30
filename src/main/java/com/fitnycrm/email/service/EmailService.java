package com.fitnycrm.email.service;

import com.fitnycrm.email.config.EmailProperties;
import com.fitnycrm.tenant.repository.entity.Tenant;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final EmailProperties emailProperties;
    private final SpringTemplateEngine templateEngine;

    @Async
    public void sendConfirmationEmail(String to, String token) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, StandardCharsets.UTF_8.name());

            Context context = new Context();
            context.setVariable("confirmationUrl",
                    String.format("%s?token=%s", emailProperties.getConfirmationUrl(), token));

            String content = templateEngine.process("email/confirmation", context);

            helper.setFrom(emailProperties.getFrom());
            helper.setTo(to);
            helper.setSubject("Confirm your email");
            helper.setText(content, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send confirmation email", e);
        }
    }

    @Async
    public void sendClientInvitation(Tenant tenant, String to, String token, String inviterName) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, StandardCharsets.UTF_8.name());

            Context context = new Context();
            context.setVariable("registrationUrl", emailProperties.getClientRegistrationUrl()
                    .formatted(tenant.getId(), token));
            context.setVariable("inviterName", inviterName);

            String content = templateEngine.process("email/client-invitation", context);

            helper.setFrom(emailProperties.getFrom());
            helper.setTo(to);
            helper.setSubject("You've been invited to FitTrack CRM by " + inviterName);
            helper.setText(content, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send client invitation email", e);
        }
    }
} 