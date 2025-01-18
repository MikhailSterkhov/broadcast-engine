package io.broadcast.wrapper.smtp.data;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class MailProperties {

    private final Boolean smtpAuth;
    private final Boolean sslEnabled;
    private final Boolean startTlsEnabled;
}
