package io.broadcast.wrapper.spring.data;

import io.broadcast.engine.record.ChunkyRecordSelector;
import io.broadcast.engine.record.Record;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@RequiredArgsConstructor
public class ChunkySpringDataRecordSelector<I, T> implements ChunkyRecordSelector<I> {

    private final ChunkySpringDataRecordMetadata<T> metadata;
    private final ReentrantLock reentrantLock = new ReentrantLock();

    @Override
    public long totalSize() {
        if (metadata.getRepository() != null) {
            return metadata.getRepository().count();
        } else {
            EntityManager entityManager = metadata.getEntityManager();

            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Long> criteria = builder.createQuery(Long.class);

            criteria.select(builder.count(criteria.from(metadata.getEntityClass())));

            return entityManager.createQuery(criteria).getSingleResult();
        }
    }

    @Override
    public long chunkSize() {
        Integer chunkSize = metadata.getChunkSize();
        if (chunkSize == null) {
            throw new BroadcastSpringDataException("metadata.chunkSize is not initialized [example=2000]");
        }
        return chunkSize;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Iterable<Record<I>> select(long index) {
        if (!reentrantLock.tryLock()) {
            return select(index);
        }
        try {
            SpringDataEntitiesService<I, T> entitiesService = SpringDataEntitiesService.create(
                    (JpaRepository<T, I>) metadata.getRepository(),
                    metadata.getEntityManager());

            List<T> entitiesResult = null;

            EntityManager entityManager = metadata.getEntityManager();

            if (metadata.getRepository() != null) {
                entitiesResult = entitiesService.getEntitiesPageListUsingRepository(chunkSize(), index);
            } else if (entityManager != null) {
                entitiesResult = entitiesService.getEntitiesListUsingNativeSQL(metadata.getEntityClass(), chunkSize(), index);
            }

            if (entitiesResult == null) {
                throw new BroadcastSpringDataException("Failed spring-data chunk selection: Not enough data");
            }

            return entitiesService.toRecordsList(entitiesResult);
        } finally {
            reentrantLock.unlock();
        }
    }
}
