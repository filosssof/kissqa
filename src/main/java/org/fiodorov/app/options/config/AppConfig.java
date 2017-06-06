package org.fiodorov.app.options.config;

import org.fiodorov.app.options.filter.CORSFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * @author rfiodorov
 *         on 2/7/17.
 */
@Configuration
@Import(value = {
       SecurityConfig.class,
        PersistenceConfig.class,
        DBConfig.class
})
@PropertySource("classpath:app.properties")
public class AppConfig {
    @Bean
    public CORSFilter corsFilter() {
        return new CORSFilter();
    }

    @Bean
    public PropertySourcesPlaceholderConfigurer properties() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
