package io.broadcast.wrapper.hibernate;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.SessionFactory;

@Getter
@Builder
@ToString
public class HibernateRecordMetadata<T> {

    private final SessionFactory sessionFactory;
    private final Class<T> entityClass;
    private final Integer chunkSize;
}
