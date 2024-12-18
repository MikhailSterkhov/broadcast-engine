package io.broadcast.wrapper.hibernate;

import io.broadcast.engine.record.ChunkyRecordSelector;
import io.broadcast.engine.record.Record;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class HibernateRecordSelector<I, T> implements ChunkyRecordSelector<I> {

    private final HibernateRecordMetadata<T> metadata;

    @Override
    public long totalSize() {
        return inSessionGet(session -> {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Long> criteria = builder.createQuery(Long.class);

            criteria.select(builder.count(criteria.from(metadata.getEntityClass())));

            return session.createQuery(criteria).getSingleResult();
        });
    }

    @Override
    public long chunkSize() {
        Integer chunkSize = metadata.getChunkSize();
        if (chunkSize == null) {
            throw new BroadcastHibernateException("metadata.chunkSize is not initialized [example=2000]");
        }
        return chunkSize;
    }

    @Override
    public Iterable<Record<I>> select(long index) {
        return inSessionGet(session -> {
            Class<T> entityClass = metadata.getEntityClass();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<T> criteriaQuery = builder.createQuery(entityClass);

            criteriaQuery.select(criteriaQuery.from(entityClass));

            Query<T> query = session.createQuery(criteriaQuery);

            query.setMaxResults((int) chunkSize());
            query.setFirstResult((int) (chunkSize() * index));

            return query.getResultStream()
                    .map(entity -> new Record<>((I)session.getIdentifier(entity)))
                    .collect(Collectors.toSet());
        });
    }

    private <V> V inSessionGet(@NotNull Function<Session, V> sessionSupply) {
        try (Session session = metadata.getSessionFactory().openSession()) {
            return sessionSupply.apply(session);
        } catch (Throwable exception) {
            throw new BroadcastHibernateException("Failed hibernate session", exception);
        }
    }
}
