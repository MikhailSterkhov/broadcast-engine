package io.broadcast.wrapper.spring.data;

import io.broadcast.engine.record.RecordObserver;
import io.broadcast.engine.record.extract.RecordExtractor;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@RequiredArgsConstructor
public class HQLSpringRecordExtractor<I, T> implements RecordExtractor<I> {

    private final Class<T> entityClass;
    private final EntityManager entityManager;

    @Override
    public void extract(@NotNull RecordObserver<I> recordObserver) {
        SpringDataEntitiesService<I, T> entitiesService = SpringDataEntitiesService.create(entityManager);
        List<T> entitiesListUsingHQL =
                entitiesService.getEntitiesListUsingHQL(entityClass);

        entitiesService.toRecordsList(entitiesListUsingHQL)
                .forEach(recordObserver::observe);
    }
}
