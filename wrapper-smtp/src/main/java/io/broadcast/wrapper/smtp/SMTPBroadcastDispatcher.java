package io.broadcast.wrapper.smtp;

import io.broadcast.engine.announcement.ContentedAnnouncement;
import io.broadcast.engine.dispatch.BroadcastDispatcher;
import io.broadcast.engine.record.Record;
import io.broadcast.wrapper.smtp.data.MailProperties;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Function;

@RequiredArgsConstructor
public class SMTPBroadcastDispatcher implements BroadcastDispatcher<String, ContentedAnnouncement<String>> {

    private final SMTPMetadata smtpMetadata;
    private Session session;

    public <T> T getMetadataProperty(T defaultValue, Function<MailProperties, T> propertyGetter) {
        MailProperties metadataProperties = smtpMetadata.getProperties();
        if (metadataProperties == null) {
            return defaultValue;
        }
        T value = propertyGetter.apply(metadataProperties);
        return value == null ? defaultValue : value;
    }

    private Session createMailSession() {
        Properties properties = new Properties();

        //properties.put("mail.transport.protocol", "smtps");
        properties.put("mail.smtp.auth", getMetadataProperty("true", mailProperties -> Boolean.toString(mailProperties.getSmtpAuth())));
        properties.put("mail.smtp.ssl.enable", getMetadataProperty("true", mailProperties -> Boolean.toString(mailProperties.getSslEnabled())));
        properties.put("mail.smtp.starttls.enable", getMetadataProperty("true", mailProperties -> Boolean.toString(mailProperties.getStartTlsEnabled())));

        properties.put("mail.smtp.user", smtpMetadata.getSenderCredentials().getUsername());
        properties.put("mail.smtp.host", smtpMetadata.getSmtpHost());
        properties.put("mail.smtp.port", smtpMetadata.getSmtpPort());

        return Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        getDefaultUserName(),
                        smtpMetadata.getSenderCredentials().getPassword());
            }
        });
    }

    @Override
    public void dispatch(@NotNull Record<String> record, @NotNull ContentedAnnouncement<String> announcement) {
        if (session == null) {
            session = createMailSession();
        }
        try {
            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress(smtpMetadata.getSenderCredentials().getEmail()));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(record.getId()));

            message.setSubject(Optional.ofNullable(announcement.getSubject()).orElse("<Non-Subject>"));
            message.setContent(announcement.getContent(), "text/html; charset=UTF-8");

            Transport.send(message);
        } catch (MessagingException exception) {
            throw new BroadcastSMTPException("Failed to send SMTP message", exception);
        }
    }
}
