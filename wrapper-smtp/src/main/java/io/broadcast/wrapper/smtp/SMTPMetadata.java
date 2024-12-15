package io.broadcast.wrapper.smtp;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class SMTPMetadata {

    private final MailCredentials senderCredentials;
    private final String subject;
    private final String smtpHost;
    private final String smtpPort;
}
