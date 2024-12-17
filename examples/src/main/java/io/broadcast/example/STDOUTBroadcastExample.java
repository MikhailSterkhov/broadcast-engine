package io.broadcast.example;

import io.broadcast.engine.BroadcastEngine;
import io.broadcast.engine.BroadcastPipeline;
import io.broadcast.engine.announcement.AnnouncementExtractor;
import io.broadcast.engine.announcement.StringAnnouncement;
import io.broadcast.engine.dispatch.BroadcastDispatcher;
import io.broadcast.engine.record.extract.RecordExtractor;
import io.broadcast.engine.record.map.RecordsMap;

import java.util.Arrays;

public class STDOUTBroadcastExample {

    private static final String[] NAMES = {"Mikhail", "Sergey", "Mark", "Andrey"};

    private static final RecordsMap<Integer, String> IMMUTABLE_RECORDS =
            RecordsMap.<Integer, String>builderHashMap()
                    .putStream(Arrays.asList(10, 20, 30, 40), idNumber -> NAMES[(idNumber / 10) - 1])
                    .build();

    public static void main(String[] args) {
        AnnouncementExtractor<StringAnnouncement> announcementExtractor =
                AnnouncementExtractor.fromID(Integer.class, (id) -> new StringAnnouncement(String.format("[ID: %s] -> \"Hello world!\"", id)));

        BroadcastPipeline<Integer, StringAnnouncement> broadcastPipeline = BroadcastPipeline.createPipeline(Integer.class, StringAnnouncement.class)
                .setDispatcher(BroadcastDispatcher.stdout())
                .setRecordExtractor(RecordExtractor.constant(IMMUTABLE_RECORDS.toRecordsSet()))
                .setAnnouncementExtractor(announcementExtractor);

        BroadcastEngine broadcastEngine = new BroadcastEngine(broadcastPipeline);
        broadcastEngine.broadcastNow();
    }
}
