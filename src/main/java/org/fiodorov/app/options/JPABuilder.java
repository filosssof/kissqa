package org.fiodorov.app.options;

import java.util.Objects;

import javax.persistence.EntityManager;

import com.querydsl.core.types.EntityPath;
import com.querydsl.jpa.impl.JPADeleteClause;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;

/**
 * @author rfiodorov
 *         on 2/7/17.
 */
public class JPABuilder {
    private final EntityManager entityManager;

    public JPABuilder(EntityManager entityManager) {
        this.entityManager = Objects.requireNonNull(entityManager, "EntityManager must be present");
    }

    public JPAUpdateClause buildUpdate(EntityPath<?> entityPath) {
        Objects.requireNonNull(entityPath, "entityPath must be present");
        return new JPAUpdateClause(entityManager, entityPath);
    }

    public JPADeleteClause buildDelete(EntityPath<?> entityPath) {
        Objects.requireNonNull(entityPath, "entityPath must be present");
        return new JPADeleteClause(entityManager, entityPath);
    }

    public JPAQueryFactory buildQuery() {
        return new JPAQueryFactory(entityManager);
    }
}
