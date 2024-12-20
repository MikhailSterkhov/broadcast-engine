package io.broadcast.example;

import io.broadcast.engine.BroadcastEngine;
import io.broadcast.engine.BroadcastPipeline;
import io.broadcast.engine.announcement.AnnouncementExtractor;
import io.broadcast.engine.announcement.StringAnnouncement;
import io.broadcast.engine.dispatch.BroadcastDispatcher;
import io.broadcast.engine.event.BroadcastEventAdapter;
import io.broadcast.engine.event.BroadcastListener;
import io.broadcast.engine.event.context.BroadcastStartEventContext;
import io.broadcast.engine.event.context.PreparedMessageEventContext;
import io.broadcast.engine.record.extract.RecordExtractor;
import io.broadcast.engine.record.map.RecordsMap;
import io.broadcast.engine.scheduler.Scheduler;
import io.broadcast.wrapper.spring.data.ChunkySpringDataRecordMetadata;
import io.broadcast.wrapper.spring.data.ChunkySpringDataRecordSelector;

import java.time.Duration;

public class SpringDataBroadcastExample {

    private static final RecordsMap<Long, HibernateBroadcastExample.Employee> employeesById = RecordsMap.newHashMap();

    public static void main(String[] args) {
        ChunkySpringDataRecordMetadata<HibernateBroadcastExample.Employee> metadata =
                ChunkySpringDataRecordMetadata.<HibernateBroadcastExample.Employee>builder()
                        .chunkSize(10)
                        .entityClass(HibernateBroadcastExample.Employee.class)
                        .entityManager(HibernateBroadcastExample.provideSessionFactory().createEntityManager())
                        .build();

        AnnouncementExtractor<StringAnnouncement> announcementExtractor = AnnouncementExtractor.fromID(Long.class,
                (id) -> new StringAnnouncement(String.format("Hello, @%s, your personal id: %d", employeesById.get(id).getUsername(), id)));

        BroadcastPipeline<Long, StringAnnouncement> broadcastPipeline = BroadcastPipeline.createPipeline(Long.class, StringAnnouncement.class)
                .setDispatcher(BroadcastDispatcher.stdout())
                .setRecordExtractor(RecordExtractor.chunkyParallel(new ChunkySpringDataRecordSelector<>(metadata)))
                .setAnnouncementExtractor(announcementExtractor)
                .addListener(BroadcastListener.stdout())
                .setScheduler(Scheduler.singleThreadScheduler());

        BroadcastEngine broadcastEngine = new BroadcastEngine(broadcastPipeline);
        broadcastEngine.scheduleBroadcastEverytime(Duration.ofSeconds(10));
    }
}
