package io.broadcast.wrapper.spring.data;

import io.broadcast.engine.record.Record;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public final class SpringDataEntitiesService<I, T> {

    public static <I, T> SpringDataEntitiesService<I, T> create(JpaRepository<T, I> jpaRepository,
                                                                EntityManager entityManager) {
        return new SpringDataEntitiesService<>(jpaRepository, entityManager);
    }

    public static <I, T> SpringDataEntitiesService<I, T> create(EntityManager entityManager) {
        return new SpringDataEntitiesService<>(null, entityManager);
    }

    public static <I, T> SpringDataEntitiesService<I, T> create(JpaRepository<T, I> jpaRepository) {
        return new SpringDataEntitiesService<>(jpaRepository, null);
    }

    private final JpaRepository<T, I> jpaRepository;
    private final EntityManager entityManager;

    private String getEntityName(Session session, Class<T> entityClass) {
        return session.getMetamodel().entity(entityClass).getName();
    }

    public Set<Record<I>> toRecordsList(List<T> entitiesResult) {
        Session session = entityManager.unwrap(Session.class);
        return entitiesResult.stream()
                .map(session::getIdentifier)
                .map(o -> new Record<>((I)o))
                .collect(Collectors.toSet());
    }

    /**
     * Получение всех сущностей через HQL (Hibernate Query Language).
     */
    public List<T> getEntitiesListUsingHQL(Class<T> entityClass) {
        Session session = entityManager.unwrap(Session.class);
        String hql = "FROM " + getEntityName(session, entityClass);
        Query<T> query = session.createQuery(hql, entityClass);
        return query.getResultList();
    }

    /**
     * Получение всех сущностей через Native SQL.
     */
    public List<T> getEntitiesListUsingNativeSQL(Class<T> entityClass) {
        Session session = entityManager.unwrap(Session.class);
        String sql = "SELECT * FROM " + getEntityName(session, entityClass);
        return session.createNativeQuery(sql, entityClass).getResultList();
    }

    /**
     * Получение всех сущностей через Native SQL.
     */
    public List<T> getEntitiesListUsingNativeSQL(Class<T> entityClass, long chunkSize, long index) {
        Session session = entityManager.unwrap(Session.class);
        String sql = "SELECT * FROM " + getEntityName(session, entityClass) + " LIMIT " + chunkSize + " OFFSET " + (chunkSize * index);
        return session.createNativeQuery(sql, entityClass).getResultList();
    }

    /**
     * Получение всех сущностей через Spring Data (JpaRepository).
     */
    public List<T> getEntitiesListUsingRepository() {
        return jpaRepository.findAll();
    }

    /**
     * Получение страницы сущностей через Spring Data (JpaRepository).
     */
    public List<T> getEntitiesPageListUsingRepository(long chunkSize, long index) {
        Page<T> page = jpaRepository.findAll(PageRequest.of((int) index, (int) chunkSize));
        return page.getContent();
    }
}
