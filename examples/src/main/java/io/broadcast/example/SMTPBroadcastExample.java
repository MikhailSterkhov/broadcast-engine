package io.broadcast.example;

import io.broadcast.engine.BroadcastEngine;
import io.broadcast.engine.BroadcastPipeline;
import io.broadcast.engine.PreparedMessage;
import io.broadcast.engine.event.ExceptionListener;
import io.broadcast.engine.record.Record;
import io.broadcast.engine.record.RecordToStringSerializer;
import io.broadcast.engine.record.extract.Extractors;
import io.broadcast.engine.scheduler.Scheduler;
import io.broadcast.wrapper.smtp.MailCredentials;
import io.broadcast.wrapper.smtp.SMTPBroadcastDispatcher;
import io.broadcast.wrapper.smtp.SMTPMetadata;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SMTPBroadcastExample {

    private static final Set<Record<String, String>> IMMUTABLE_RECORDS =
            new HashSet<>(
                    Arrays.asList(
                            new Record<>("<recipient-mail-address>", "John Doe")
                    )
            );

    public static void main(String[] args) {
        SMTPMetadata smtpMetadata = SMTPMetadata.builder()
                .senderCredentials(MailCredentials.builder()
                        .username("<mail-username>")
                        .email("<mail-address>")
                        .password("<mail-password>")
                        .build())
                .smtpHost("smtp.yandex.ru")
                .smtpPort("465")
                .build();

        PreparedMessage<Integer, String> preparedMessage
                = PreparedMessage.serialize(
                        RecordToStringSerializer.single("<Message-Subject>"),
                        (record) -> String.format("Hello, %s! ", record.getId()));

        BroadcastPipeline broadcastPipeline = BroadcastPipeline.createPipeline()
                .setDispatcher(new SMTPBroadcastDispatcher<>(smtpMetadata))
                .setRecordExtractor(Extractors.immutable(IMMUTABLE_RECORDS))
                .setPreparedMessage(preparedMessage)
                .addListener(new ExceptionListener() {
                    @Override
                    public void throwsException(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                })
                .setScheduler(Scheduler.defaultScheduler());

        BroadcastEngine broadcastEngine = new BroadcastEngine(broadcastPipeline);
        broadcastEngine.scheduleBroadcastEverytime(Duration.ofDays(1));
    }
}
