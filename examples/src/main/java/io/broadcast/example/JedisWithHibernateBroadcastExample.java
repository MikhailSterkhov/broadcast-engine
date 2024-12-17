package io.broadcast.example;

import io.broadcast.engine.BroadcastEngine;
import io.broadcast.engine.announcement.AnnouncementExtractor;
import io.broadcast.engine.announcement.StringAnnouncement;
import io.broadcast.engine.record.extract.RecordExtractor;
import io.broadcast.wrapper.hibernate.HibernateRecordMetadata;
import io.broadcast.wrapper.hibernate.HibernateRecordSelector;
import io.broadcast.wrapper.jeds.JedisBroadcastPipeline;
import io.broadcast.wrapper.jeds.dispatcher.JedisDispatcher;
import redis.clients.jedis.Jedis;

public class JedisWithHibernateBroadcastExample {

    public static void main(String[] args) {
        Jedis jedis = new Jedis();

        AnnouncementExtractor<StringAnnouncement> announcementExtractor =
                AnnouncementExtractor.fromID(String.class, (id) -> new StringAnnouncement(String.format("[ID: %s] -> \"Hello world!\"", id)));

        HibernateRecordMetadata<HibernateBroadcastExample.Employee> hibernateRecordMetadata =
                HibernateRecordMetadata.<HibernateBroadcastExample.Employee>builder()
                        .chunkSize(100)
                        .entityClass(HibernateBroadcastExample.Employee.class)
                        .sessionFactory(HibernateBroadcastExample.provideSessionFactory())
                        .build();

        JedisBroadcastPipeline broadcastPipeline = JedisBroadcastPipeline.createPipeline()
                .setAnnouncementExtractor(announcementExtractor)
                .setDispatcher(JedisDispatcher.publishQueue(jedis, "players_queue"))
                .setRecordExtractor(RecordExtractor.chunkyParallel(new HibernateRecordSelector<>(hibernateRecordMetadata)));

        BroadcastEngine broadcastEngine = new BroadcastEngine(broadcastPipeline);
        broadcastEngine.broadcastNow();
    }
}
