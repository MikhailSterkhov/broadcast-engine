package io.broadcast.wrapper.smtp;

import io.broadcast.wrapper.smtp.data.MailCredentials;
import io.broadcast.wrapper.smtp.data.MailProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class SMTPMetadata {

    private final MailCredentials senderCredentials;

    private final MailProperties properties;

    private final String smtpHost;
    private final String smtpPort;
}
