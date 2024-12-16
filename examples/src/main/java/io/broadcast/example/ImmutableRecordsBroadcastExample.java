package io.broadcast.example;

import io.broadcast.engine.BroadcastEngine;
import io.broadcast.engine.BroadcastPipeline;
import io.broadcast.engine.PreparedMessage;
import io.broadcast.engine.dispatch.STDOUTBroadcastDispatcher;
import io.broadcast.engine.record.Record;
import io.broadcast.engine.record.extract.Extractors;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class ImmutableRecordsBroadcastExample {

    private static final Set<Record<Integer, String>> IMMUTABLE_RECORDS =
            new HashSet<>(
                    Arrays.asList(
                            Record.ofInt("Mikhail", (s) -> ThreadLocalRandom.current().nextInt()),
                            Record.ofInt("Sergey", (s) -> ThreadLocalRandom.current().nextInt()),
                            Record.ofInt("Mark", (s) -> ThreadLocalRandom.current().nextInt()),
                            Record.ofInt("Andrey", (s) -> ThreadLocalRandom.current().nextInt())
                    )
            );

    public static void main(String[] args) {
        PreparedMessage<Integer, String> preparedMessage
                = PreparedMessage.serializeContent(
                (record) -> String.format("[ID: %s] -> \"Hello world!\"", record.getId()));

        BroadcastPipeline broadcastPipeline = BroadcastPipeline.createPipeline()
                .setDispatcher(new STDOUTBroadcastDispatcher<>())
                .setRecordExtractor(Extractors.immutable(IMMUTABLE_RECORDS))
                .setPreparedMessage(preparedMessage);

        BroadcastEngine broadcastEngine = new BroadcastEngine(broadcastPipeline);
        broadcastEngine.broadcastNow();
    }
}
