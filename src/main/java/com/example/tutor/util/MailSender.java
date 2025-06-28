package com.example.tutor.util;

import com.sun.mail.smtp.SMTPAddressFailedException;
import jakarta.mail.MessagingException;
import jakarta.mail.SendFailedException;
import jakarta.mail.internet.MimeMessage;
import com.example.tutor.exception.EmailSendException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class MailSender {
    private final JavaMailSender emailSender;
    @Value("${spring.mail.username}")
    private String sender;

    @Value("${test.mode}")
    private boolean testMode;

    @Value("${activation.link.test}")
    private String linkTest;

    @Value("${activation.link.prod}")
    private String linkProd;

    @Value("${reset.password.link.prod}")
    private String resetLinkProd;

    @Value("${reset.password.link.test}")
    private String resetLinkTest;

    public void sendActivationLink(String mail, UUID code) {
        try {
            MimeMessage mimeMessage = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "utf-8");

            helper.setFrom(sender);
            helper.setTo(mail);

            String activationLink = String.format(testMode ? linkTest : linkProd, code);
            helper.setText(activationLink, false);
            helper.setSubject("Ссылка для активации профиля");

            emailSender.send(mimeMessage);

        } catch (MailSendException e) {
            if (e.getCause() instanceof SendFailedException sfe &&
                    sfe.getNextException() instanceof SMTPAddressFailedException smtpEx &&
                    smtpEx.getMessage().contains("550")) {
                throw new EmailSendException("error.user.not_found.email");
            }
            throw new EmailSendException("error.mail.send_failed");

        } catch (MessagingException e) {
            throw new EmailSendException("error.mail.send_failed");
        }
    }

    public void sendPasswordResetLink(String mail, String token, Integer passwordLength) throws MessagingException {
        try {
            MimeMessage mimeMessage = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "utf-8");

            helper.setFrom(sender);
            helper.setTo(mail);

            String baseLink = testMode ? resetLinkTest : resetLinkProd;
            String linkWithParam = String.format(baseLink, token) + "&length=" + passwordLength;

            helper.setText(linkWithParam, false);
            helper.setSubject("Ссылка для сброса пароля");

            emailSender.send(mimeMessage);

        } catch (MailSendException e) {
            if (e.getCause() instanceof SendFailedException sfe &&
                    sfe.getNextException() instanceof SMTPAddressFailedException smtpEx &&
                    smtpEx.getMessage().contains("550")) {
                throw new EmailSendException("error.user.not_found.email");
            }
            throw new EmailSendException("error.mail.send_failed");

        } catch (MessagingException e) {
            throw new EmailSendException("error.mail.send_failed");
        }
    }

}
