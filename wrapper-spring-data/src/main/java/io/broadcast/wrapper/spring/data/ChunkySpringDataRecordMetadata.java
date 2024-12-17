package io.broadcast.wrapper.spring.data;

import jakarta.persistence.EntityManager;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.jpa.repository.JpaRepository;

@Getter
@Builder
@ToString
public class ChunkySpringDataRecordMetadata<T> {

    private final EntityManager entityManager;
    private final JpaRepository<T, ?> repository;
    private final Class<T> entityClass;
    private final Integer chunkSize;
}
