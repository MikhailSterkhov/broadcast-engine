package io.broadcast.wrapper.smtp;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class MailCredentials {

    private final String username;
    private final String email;
    private final String password;
}
