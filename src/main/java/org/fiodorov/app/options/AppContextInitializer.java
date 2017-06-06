package org.fiodorov.app.options;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.util.StringUtils;


/**
 * @author rfiodorov
 *         on 2/7/17.
 */
public class AppContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final Logger logger = LoggerFactory.getLogger(AppContextInitializer.class);

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        String configPath = environment.getProperty("enjo.config.path", "/opt/enjo/config/");

        try {
            ResourcePropertySource propertySource = new ResourcePropertySource("file:" + configPath + "application.properties");
            environment.getPropertySources().addLast(propertySource);

            configureLog4j2(environment);

            logger.info("{} loaded", propertySource.getName());
        } catch (IOException e) {
            // it's ok if the file is not there. we will just log that info.
            logger.info("didn't find {} application.properties in classpath so not loading it in the AppContextInitialized", configPath, e);
        }
    }

    private void configureLog4j2(Environment environment) {
        String log4jConfigPath = environment.getProperty("log4j2.config.path");
        if (!StringUtils.isEmpty(log4jConfigPath)) {
            LoggerContext context = (org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false);
            File file = new File(log4jConfigPath);
            context.setConfigLocation(file.toURI());
        }
    }

}
