package org.fiodorov.app.options.config;

import java.util.List;

import org.fiodorov.app.options.GlobalControllerExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.mobile.device.DeviceWebArgumentResolver;
import org.springframework.mobile.device.site.SitePreferenceHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.ServletWebArgumentResolverAdapter;

/**
 * @author rfiodorov
 *         on 2/7/17.
 */
@Import(value = {
        ValidationConfig.class,
        ObjectMapperConfig.class,
        ControllerConfig.class})
@EnableWebMvc
@EnableSpringDataWebSupport
@Configuration
@EnableAspectJAutoProxy
public class AppServletConfig extends WebMvcConfigurerAdapter {

//    @Autowired
//    private Environment env;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        if (env.getProperty("db.showSql", Boolean.class, false)) {
////            registry.addInterceptor(new SLF4JQueryCountLoggingHandlerInterceptor());
//        }
    }

    @Bean
    public SitePreferenceHandlerMethodArgumentResolver sitePreferenceHandlerMethodArgumentResolver() {
        return new SitePreferenceHandlerMethodArgumentResolver();
    }

    @Bean
    public ServletWebArgumentResolverAdapter servletWebArgumentResolverAdapter() {
        return new ServletWebArgumentResolverAdapter(deviceWebArgumentResolver());
    }

    @Bean
    public DeviceWebArgumentResolver deviceWebArgumentResolver() {
        return new DeviceWebArgumentResolver();
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(servletWebArgumentResolverAdapter());
        argumentResolvers.add(sitePreferenceHandlerMethodArgumentResolver());
    }

    @Bean
    public GlobalControllerExceptionHandler exceptionHandler() {
        return new GlobalControllerExceptionHandler();
    }
}
