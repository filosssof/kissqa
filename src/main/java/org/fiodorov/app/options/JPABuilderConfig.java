package org.fiodorov.app.options;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author rfiodorov
 *         on 2/7/17.
 */
@Configuration
public class JPABuilderConfig {

    @PersistenceContext(unitName = "mind_pu")
    private EntityManager entityManager;

    @Bean
    public JPABuilder jpaBuilder() {
        return new JPABuilder(entityManager);
    }

}
