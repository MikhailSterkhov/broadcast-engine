package io.broadcast.example;

import io.broadcast.engine.BroadcastEngine;
import io.broadcast.engine.BroadcastPipeline;
import io.broadcast.engine.announcement.AnnouncementExtractor;
import io.broadcast.engine.announcement.StringAnnouncement;
import io.broadcast.engine.dispatch.BroadcastDispatcher;
import io.broadcast.engine.event.BroadcastEventAdapter;
import io.broadcast.engine.event.context.BroadcastStartEventContext;
import io.broadcast.engine.event.context.PreparedMessageEventContext;
import io.broadcast.engine.record.extract.RecordExtractor;
import io.broadcast.engine.record.map.RecordsMap;
import io.broadcast.engine.scheduler.Scheduler;
import io.broadcast.wrapper.hibernate.BroadcastHibernateException;
import io.broadcast.wrapper.spring.data.ChunkySpringDataRecordMetadata;
import io.broadcast.wrapper.spring.data.ChunkySpringDataRecordSelector;
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

public class SpringDataBroadcastExample {

    private static final RecordsMap<Long, Employee> employeesById = RecordsMap.newHashMap();

    public static void main(String[] args) {
        ChunkySpringDataRecordMetadata<Employee> metadata =
                ChunkySpringDataRecordMetadata.<Employee>builder()
                        .chunkSize(10)
                        .entityClass(Employee.class)
                        .entityManager(provideSessionFactory().createEntityManager())
                        .build();

        AnnouncementExtractor<StringAnnouncement> announcementExtractor = AnnouncementExtractor.fromID(Long.class,
                (id) -> new StringAnnouncement(String.format("Hello, @%s, your personal id: %d", employeesById.get(id).getUsername(), id)));

        BroadcastPipeline<Long, StringAnnouncement> broadcastPipeline = BroadcastPipeline.createPipeline(Long.class, StringAnnouncement.class)
                .setDispatcher(BroadcastDispatcher.stdout())
                .setRecordExtractor(RecordExtractor.chunkyParallel(new ChunkySpringDataRecordSelector<>(metadata)))
                .setAnnouncementExtractor(announcementExtractor)
                .addListener(new BroadcastEventAdapter() {
                    @Override
                    public void broadcastStart(BroadcastStartEventContext eventContext) {
                        System.out.println(eventContext);
                    }

                    @Override
                    public void preparedMessage(PreparedMessageEventContext eventContext) {
                        System.out.println(eventContext);
                    }
                })
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
                    session.persist("Employee", new Employee(username));

                    Employee employee = session.find(Employee.class, i + 1);

                    employeesById.put(employee.getId(), employee);
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
