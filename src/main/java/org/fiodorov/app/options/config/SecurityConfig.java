package org.fiodorov.app.options.config;

import org.fiodorov.app.options.CustomAccessDeniedHandler;
import org.fiodorov.app.options.GlobalControllerExceptionHandler;
import org.fiodorov.app.options.TokenRefreshService;
import org.fiodorov.app.options.UserRefreshTokenFilter;
import org.fiodorov.app.options.auth.AppAuthenticationManager;
import org.fiodorov.app.options.auth.AuthenticationErrorHandler;
import org.fiodorov.app.options.auth.AuthenticationFilter;
import org.fiodorov.app.options.auth.ClientAuthenticationSuccessCustomHandler;
import org.fiodorov.app.options.auth.LoginUserFilter;
import org.fiodorov.app.options.auth.TokenAuthenticationService;
import org.fiodorov.app.options.auth.UserRole;
import org.fiodorov.repository.UserRepository;
import org.fiodorov.service.api.LoginServiceApi;
import org.fiodorov.service.impl.LoginServiceImpl;
import org.fiodorov.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author rfiodorov
 *         on 2/7/17.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Order(1)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${token.secret}")
    private String secretKey;

    @Value("${token.expirationTime}")
    private String expirationTime;

    @Autowired
    private UserRepository userRepository;

    public SecurityConfig() {
        super(true);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.exceptionHandling().authenticationEntryPoint(new AuthenticationErrorHandler()).and()
                .authorizeRequests().antMatchers("/resources/**").permitAll()
                // Define rules
                .antMatchers(HttpMethod.POST, "/facebook/login").permitAll()
                .antMatchers(HttpMethod.POST, "/facebook/signup").permitAll()
                .antMatchers(HttpMethod.POST, "/user/signup").permitAll()
                .antMatchers(HttpMethod.POST, "/user/login").permitAll()
                .antMatchers(HttpMethod.GET,"/user").authenticated()
                .antMatchers(HttpMethod.GET,"/questions").permitAll()
                .antMatchers(HttpMethod.POST,"/questions").authenticated()
                .antMatchers(HttpMethod.GET,"/questions/**").permitAll()
                .antMatchers(HttpMethod.POST,"/questions/**").authenticated()
                .antMatchers(HttpMethod.POST,"/questions/vote").authenticated()
                .antMatchers(HttpMethod.POST,"/answers").authenticated()
                .antMatchers(HttpMethod.POST,"/answers/vote").authenticated()
                .antMatchers(HttpMethod.GET,"/answers/**/rank").authenticated()
                .antMatchers(HttpMethod.POST,"/answers/**/best").authenticated()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers(HttpMethod.GET, "/**")
                .hasAnyRole(UserRole.SIMPLE.name(), UserRole.FACEBOOK_CLIENT.name(), UserRole.ADMIN.name())
                .antMatchers(HttpMethod.POST, "/**")
                .hasAnyRole(UserRole.SIMPLE.name(), UserRole.FACEBOOK_CLIENT.name(), UserRole.ADMIN.name())
                .antMatchers(HttpMethod.PUT, "/**")
                .hasAnyRole(UserRole.SIMPLE.name(), UserRole.FACEBOOK_CLIENT.name(), UserRole.ADMIN.name())
                .antMatchers(HttpMethod.PATCH, "/**")
                .hasAnyRole(UserRole.SIMPLE.name(), UserRole.FACEBOOK_CLIENT.name(), UserRole.ADMIN.name())
                .and()

                .addFilterBefore(loginUserFilter(authenticationManagerBean()),
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(
                        new UserRefreshTokenFilter("/refreshToken", tokenRefreshService(), authenticationManager(), userDetailsService()),
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new AuthenticationFilter(tokenAuthenticationService()),
                        UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling().accessDeniedHandler(customAccessDeniedHandler()).and().anonymous().and()
                .servletApi().and()
                .headers().cacheControl().and();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return new AppAuthenticationManager(userRepository);
    }


    @Bean
    public TokenAuthenticationService tokenAuthenticationService() {
        return new TokenAuthenticationService(secretKey, expirationTime);
    }

    @Bean
    public CustomAccessDeniedHandler customAccessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

    @Bean
    public TokenRefreshService tokenRefreshService() {
        return new TokenRefreshService(secretKey, expirationTime);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService());
    }

    @Bean
    public GlobalControllerExceptionHandler exceptionHandler() {
        return new GlobalControllerExceptionHandler();
    }

    @Bean
    public LoginUserFilter loginUserFilter(AuthenticationManager authenticationManager) {
        LoginUserFilter loginUserFilter = new LoginUserFilter("/user/login", tokenAuthenticationService(),
                new UserDetailsServiceImpl(userRepository), authenticationManager,userRepository);
        loginUserFilter.setAuthenticationSuccessHandler(new ClientAuthenticationSuccessCustomHandler());
        loginUserFilter.setContinueChainBeforeSuccessfulAuthentication(false);
        return loginUserFilter;
    }

    @Bean
    public LoginServiceApi loginServiceApi(){
        return new LoginServiceImpl(userRepository, tokenAuthenticationService());
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService(){
        return new UserDetailsServiceImpl(userRepository);
    }
}
