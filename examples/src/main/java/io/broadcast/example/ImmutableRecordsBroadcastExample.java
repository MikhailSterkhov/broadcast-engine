package io.broadcast.example;

import io.broadcast.engine.BroadcastEngine;
import io.broadcast.engine.BroadcastPipeline;
import io.broadcast.engine.PreparedMessage;
import io.broadcast.engine.dispatch.STDOUTBroadcastDispatcher;
import io.broadcast.engine.record.extract.Extractors;
import io.broadcast.engine.record.map.RecordsMap;

import java.util.Arrays;

public class ImmutableRecordsBroadcastExample {

    private static final String[] NAMES = {"Mikhail", "Sergey", "Mark", "Andrey"};

    private static final RecordsMap<Integer, String> IMMUTABLE_RECORDS =
            RecordsMap.<Integer, String>builderHashMap()
                    .putStream(Arrays.asList(10, 20, 30, 40), idNumber -> NAMES[(idNumber / 10) - 1])
                    .build();

    public static void main(String[] args) {
        PreparedMessage<Integer> preparedMessage
                = PreparedMessage.serializeContent((record) -> String.format("[ID: %s] -> \"Hello world!\"", record.getId()));

        BroadcastPipeline broadcastPipeline = BroadcastPipeline.createPipeline()
                .setDispatcher(new STDOUTBroadcastDispatcher<>())
                .setRecordExtractor(Extractors.immutable(IMMUTABLE_RECORDS.toRecordsSet()))
                .setPreparedMessage(preparedMessage);

        BroadcastEngine broadcastEngine = new BroadcastEngine(broadcastPipeline);
        broadcastEngine.broadcastNow();
    }
}
