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

public class ImmutableRecordsBroadcastExample {

    private static final Set<Record<Integer, String>> IMMUTABLE_RECORDS =
            new HashSet<>(
                    Arrays.asList(
                            Record.ofInt("Mikhail", Object::hashCode),
                            Record.ofInt("Sergey", Object::hashCode),
                            Record.ofInt("Mark", Object::hashCode),
                            Record.ofInt("Andrey", Object::hashCode)
                    )
            );

    public static void main(String[] args) {
        PreparedMessage<Integer, String> preparedMessage
                = PreparedMessage.func(
                (record) -> String.format("[ID: %s] -> \"Hello world!\"", record.getId()));

        BroadcastPipeline broadcastPipeline = BroadcastPipeline.createPipeline()
                .setDispatcher(new STDOUTBroadcastDispatcher<>())
                .setRecordExtractor(Extractors.immutable(IMMUTABLE_RECORDS))
                .setPreparedMessage(preparedMessage);

        BroadcastEngine broadcastEngine = new BroadcastEngine(broadcastPipeline);
        broadcastEngine.broadcastNow();
    }
}
