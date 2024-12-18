package io.broadcast.example.streaming;

import io.broadcast.engine.record.extract.RecordExtractor;
import io.broadcast.engine.streaming.Streaming;
import io.broadcast.engine.streaming.StreamingChannel;
import io.broadcast.engine.streaming.fork.ForkedSession;
import io.broadcast.engine.streaming.fork.Recipient;
import io.broadcast.engine.streaming.fork.Source;
import io.broadcast.engine.streaming.fork.Timeline;
import io.broadcast.example.HibernateBroadcastExample;
import io.broadcast.wrapper.hibernate.HibernateRecordMetadata;
import io.broadcast.wrapper.hibernate.HibernateRecordSelector;
import io.broadcast.wrapper.jeds.dispatcher.JedisDispatcher;
import redis.clients.jedis.Jedis;

import java.util.concurrent.CompletableFuture;

public class HibernateToJedisStreamingExample {

    @SuppressWarnings("DataFlowIssue")
    public static void main(String[] args) {

        Jedis jedis = new Jedis();
        StreamingChannel streamingChannel = null;

        HibernateRecordMetadata<HibernateBroadcastExample.Employee> hibernateRecordMetadata =
                HibernateRecordMetadata.<HibernateBroadcastExample.Employee>builder()
                        .chunkSize(100)
                        .entityClass(HibernateBroadcastExample.Employee.class)
                        .sessionFactory(HibernateBroadcastExample.provideSessionFactory())
                        .build();

        Streaming streaming = streamingChannel
                .from(Source.extracts(RecordExtractor.chunkyParallel(new HibernateRecordSelector<>(hibernateRecordMetadata))))
                .to(Recipient.dispatches(JedisDispatcher.publishQueue(jedis)))
                .at(Timeline.replicationMode())
                .start();

        ForkedSession forkedSession = streaming.lastProceedSession();
        CompletableFuture<ForkedSession> forkedSessionFuture = streaming.futureFork();
    }
}
