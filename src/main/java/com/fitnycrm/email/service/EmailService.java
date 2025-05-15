package com.fitnycrm.email.service;

import com.fitnycrm.email.config.EmailProperties;
import com.fitnycrm.tenant.repository.entity.Tenant;
import com.fitnycrm.user.repository.entity.ClientInvitation;
import com.fitnycrm.user.repository.entity.User;
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
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final EmailProperties emailProperties;
    private final SpringTemplateEngine templateEngine;

    @Async
    public void sendConfirmationEmail(String to, String token, Locale locale) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, StandardCharsets.UTF_8.name());

            Context context = new Context();
            context.setVariable("confirmationUrl",
                    String.format("%s?token=%s", emailProperties.getConfirmationUrl(), token));

            String content = templateEngine.process("email/%s/confirmation".formatted(locale.getLanguage()), context);

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
    public void sendClientInvitation(Tenant tenant, ClientInvitation invitation, User inviter) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, StandardCharsets.UTF_8.name());

            Context context = new Context();
            context.setVariable("registrationUrl", emailProperties.getClientRegistrationUrl()
                    .formatted(tenant.getId(), invitation.getId()));
            String inviterFullName = inviter.getFirstName() + " " + inviter.getLastName();
            context.setVariable("inviterName", inviterFullName);
            context.setVariable("tenantName", tenant.getName());

            String content = templateEngine.process("email/%s/client-invitation"
                    .formatted(tenant.getLocale().getLanguage()), context);

            helper.setFrom(emailProperties.getFrom());
            helper.setTo(invitation.getEmail());
            helper.setSubject("You've been invited to " + tenant.getName() + " by " + inviterFullName);
            helper.setText(content, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send client invitation email", e);
        }
    }
} 