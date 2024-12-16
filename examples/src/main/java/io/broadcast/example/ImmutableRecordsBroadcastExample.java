package io.broadcast.example;

import io.broadcast.engine.BroadcastEngine;
import io.broadcast.engine.BroadcastPipeline;
import io.broadcast.engine.announcement.AnnouncementExtractor;
import io.broadcast.engine.announcement.StringAnnouncement;
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
        AnnouncementExtractor<StringAnnouncement> announcementExtractor =
                AnnouncementExtractor.fromID(Integer.class, (id) -> new StringAnnouncement(String.format("[ID: %s] -> \"Hello world!\"", id)));

        BroadcastPipeline<Integer> broadcastPipeline = BroadcastPipeline.createPipeline(Integer.class)
                .setDispatcher(new STDOUTBroadcastDispatcher<>())
                .setRecordExtractor(Extractors.constant(IMMUTABLE_RECORDS.toRecordsSet()))
                .setAnnouncementExtractor(announcementExtractor);

        BroadcastEngine broadcastEngine = new BroadcastEngine(broadcastPipeline);
        broadcastEngine.broadcastNow();
    }
}
