package org.fiodorov.app.options.config;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.fiodorov.app.options.JPABuilderConfig;
import org.fiodorov.repository.MarkerJPADomainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author rfiodorov
 *         on 2/7/17.
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackageClasses = MarkerJPADomainRepository.class)
@Import({ JPABuilderConfig.class })
public class PersistenceConfig {

    private static final String PERSISTENCE_UNIT_NAME = "mind_pu";

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JpaVendorAdapter jpaVendorAdapter;

    @Bean
    public EntityManagerFactory entityManagerFactory() {

        Map<String, Object> map = new HashMap<>();

        // needed for validity auditing aka hibernate envers



        LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
        bean.setDataSource(dataSource);
        bean.setJpaVendorAdapter(jpaVendorAdapter);
        bean.setPersistenceUnitName(PERSISTENCE_UNIT_NAME);
        bean.setJpaPropertyMap(map);
        bean.afterPropertiesSet();

        return bean.getObject();
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new JpaTransactionManager(entityManagerFactory());
    }
}
