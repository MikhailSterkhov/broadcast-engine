package io.broadcast.example;

import io.broadcast.engine.BroadcastEngine;
import io.broadcast.engine.BroadcastPipeline;
import io.broadcast.engine.PreparedMessage;
import io.broadcast.engine.dispatch.STDOUTBroadcastDispatcher;
import io.broadcast.engine.record.extract.Extractors;
import io.broadcast.engine.scheduler.Scheduler;
import io.broadcast.wrapper.hibernate.BroadcastHibernateException;
import io.broadcast.wrapper.hibernate.HibernateRecordMetadata;
import io.broadcast.wrapper.hibernate.HibernateRecordSelector;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;
import net.bytebuddy.utility.RandomString;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.time.Duration;

public class HibernateBroadcastExample {

    public static void main(String[] args) {
        HibernateRecordMetadata<Employee> metadata =
                HibernateRecordMetadata.<Employee>builder()
                        .chunkSize(10)
                        .entityClass(Employee.class)
                        .sessionFactory(provideSessionFactory())
                        .build();

        PreparedMessage<Long, Employee> preparedMessage
                = PreparedMessage.serializeContent((record) -> String.format("Hello, @%s, your personal id: %d", record.getEntity().getUsername(), record.getId()));

        BroadcastPipeline broadcastPipeline = BroadcastPipeline.createPipeline()
                .setDispatcher(new STDOUTBroadcastDispatcher<>())
                .setRecordExtractor(Extractors.chunkyParallel(new HibernateRecordSelector<>(metadata)))
                .setPreparedMessage(preparedMessage)
                .setScheduler(Scheduler.singleThreadScheduler());

        BroadcastEngine broadcastEngine = new BroadcastEngine(broadcastPipeline);
        broadcastEngine.scheduleBroadcastEverytime(Duration.ofSeconds(10));
    }

    public static SessionFactory provideSessionFactory() {
        SessionFactory sessionFactory;
        try {
            sessionFactory = new Configuration()
                    .addAnnotatedClass(Employee.class)
                    .configure()
                    .buildSessionFactory();

            try (Session session = sessionFactory.openSession()) {
                Transaction transaction = session.beginTransaction();

                for (int i = 0; i < 100; i++) {

                    String username = RandomString.make(16);
                    session.persist(new Employee(username));
                }

                transaction.commit();

            } catch (HibernateException hibernateException) {
                throw new RuntimeException(hibernateException);
            }
        } catch (Throwable exception) {
            throw new BroadcastHibernateException(exception);
        }
        return sessionFactory;
    }

    @Getter
    @ToString
    @Entity
    @Table(name = "employees")
    public static class Employee {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String username;

        public Employee() { }

        public Employee(String username) {
            this.username = username;
        }
    }
}
