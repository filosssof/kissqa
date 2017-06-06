package org.fiodorov.app.options.config;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

/**
 * @author rfiodorov
 *         on 2/7/17.
 */
@Configuration
@PropertySource("classpath:db.properties")
public class DBConfig {

    @Autowired
    private Environment env;

    @Bean(destroyMethod = "")
    public DataSource dataSource() {
            return createBasicDataSource();
    }

    private DataSource createBasicDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl(env.getRequiredProperty("db.url"));
        dataSource.setUsername(env.getRequiredProperty("db.user"));
        dataSource.setPassword(env.getRequiredProperty("db.password"));
        dataSource.setMaxTotal(200);
        dataSource.setMaxIdle(200);
        dataSource.setMaxWaitMillis(2000);
        return dataSource;
    }

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter vendor = new HibernateJpaVendorAdapter();
        vendor.setDatabasePlatform("org.hibernate.dialect.PostgreSQLDialect");
        vendor.setGenerateDdl(false);
        vendor.setShowSql(Boolean.valueOf(env.getRequiredProperty("db.show_sql")));
        return vendor;
    }

}

