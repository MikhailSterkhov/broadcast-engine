package io.broadcast.wrapper.spring.data;

import io.broadcast.engine.record.RecordObserver;
import io.broadcast.engine.record.extract.RecordExtractor;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@RequiredArgsConstructor
public class JpaRepositoryRecordExtractor<I, T> implements RecordExtractor<I> {

    private final Class<T> entityClass;
    private final JpaRepository<T, I> repository;

    @Override
    public void extract(@NotNull RecordObserver<I> recordObserver) {
        SpringDataEntitiesService<I, T> entitiesService = SpringDataEntitiesService.create(repository);
        List<T> entitiesListUsingHQL =
                entitiesService.getEntitiesListUsingRepository();

        entitiesService.toRecordsList(entitiesListUsingHQL)
                .forEach(recordObserver::observe);
    }
}
