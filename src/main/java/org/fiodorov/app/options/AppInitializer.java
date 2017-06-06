package org.fiodorov.app.options;

import javax.servlet.FilterRegistration;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

import org.fiodorov.app.options.config.AppConfig;
import org.fiodorov.app.options.config.AppServletConfig;
import org.fiodorov.app.options.filter.CORSFilter;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * @author rfiodorov on 6/17/16.
 */
public class AppInitializer extends AbstractSecurityWebApplicationInitializer {


    @Override
    public void afterSpringSecurityFilterChain(ServletContext servletContext) {
        super.afterSpringSecurityFilterChain(servletContext);
        // servlet registration
        ServletRegistration.Dynamic servletConfig = servletContext.addServlet("appServlet", DispatcherServlet.class);
        servletConfig.setInitParameter("contextClass", AnnotationConfigWebApplicationContext.class.getName());
        servletConfig.setInitParameter("contextConfigLocation", AppServletConfig.class.getName());
        servletConfig.addMapping("/");
        servletConfig.setLoadOnStartup(1);

        servletConfig.setMultipartConfig(new MultipartConfigElement((String) null));

        // HttpMethodFilter
        FilterRegistration.Dynamic httpMethodFilter = servletContext.addFilter("HttpMethodFilter", HiddenHttpMethodFilter.class);
        httpMethodFilter.addMappingForUrlPatterns(null, false, "/*");

        servletContext.addListener(new RequestContextListener());
        servletContext.addListener(new HttpSessionEventPublisher());


        // parameter used for logging framewark
        System.setProperty("app.name", "kissqa");

    }

    @Override
    protected void beforeSpringSecurityFilterChain(ServletContext servletContext) {
        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.register(AppConfig.class);
        ContextLoaderListener contextLoaderListener = new ContextLoaderListener(rootContext);
        servletContext.addListener(contextLoaderListener);
        servletContext.setInitParameter("contextInitializerClasses", AppContextInitializer.class.getName());

        // CORS Filter
        FilterRegistration.Dynamic corsFilter = servletContext.addFilter("CORSFilter", CORSFilter.class);
        corsFilter.addMappingForUrlPatterns(null, false, "/*");

    }


}
