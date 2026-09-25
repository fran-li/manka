package pe.edu.utec.manka.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.TemplateEngine;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Service
public class EmailService {
    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final boolean enabled;
    private final String from;

    public EmailService(JavaMailSender mailSender,
                        TemplateEngine templateEngine,
                        @Value("${manka.mail.enabled:true}") boolean enabled,
                        @Value("${manka.mail.from:noreply@manka.local}") String from) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.enabled = enabled;
        this.from = from;
    }

    public void sendWelcomeEmail(String to, String firstName) {
        Context context = new Context();
        context.setVariable("firstName", firstName);
        String html = templateEngine.process("email/welcome", context);
        sendHtml(to, "Bienvenido a Manka", html);
    }

    public void sendDishCookedEmail(String to,
                                    String firstName,
                                    String dishName,
                                    LocalDateTime cookedAt) {
        Context context = new Context();
        context.setVariable("firstName", firstName);
        context.setVariable("dishName", dishName);
        context.setVariable("cookedAt", cookedAt);
        String html = templateEngine.process("email/dish-cooked", context);
        sendHtml(to, "Manka registró tu plato cocinado", html);
    }

    private void sendHtml(String to, String subject, String html) {
        if (!enabled) {
            log.info("Email disabled. Skipping message to={} subject={}", to, subject);
            return;
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    false,
                    StandardCharsets.UTF_8.name()
            );
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(html, true);
            mailSender.send(message);
            log.info("Async email sent to={} subject={}", to, subject);
        } catch (MessagingException | MailException ex) {
            log.error("Could not send async email to={} subject={}: {}", to, subject, ex.getMessage());
        }
    }
}
