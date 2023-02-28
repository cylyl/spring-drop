package com.github.cylyl.springdrop.acl.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfiguration extends BaseSecurityConfiguration {

    /**
     * Below is an example configuration using the WebSecurityConfigurerAdapter that ignores requests that match /ignore1 or /ignore2:
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .antMatchers( "/h2-console/**")
                .antMatchers( "/ignore1", "/ignore2")
                ;
    }

    /**
     * Below is an example configuration using the WebSecurityConfigurerAdapter that secures all endpoints with HTTP Basic:
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //https://docs.spring.io/spring-security/reference/servlet/authorization/authorize-http-requests.html
        http.authorizeHttpRequests((authz) ->
//        http.authorizeRequests((authz) ->
                        authz
//                                .expressionHandler(webExpressionHandler())
                                .mvcMatchers("/account/**").hasRole("ADMIN")
                                .mvcMatchers("/**").hasRole("ADMIN")
                                .anyRequest()
                                .denyAll()
//                                .authenticated()
                )
//                .csrf().disable()
                .httpBasic(withDefaults())
        ;

        return http.build();
    }

    @Bean
    public RoleHierarchyImpl roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER");
        return roleHierarchy;
    }

    private SecurityExpressionHandler<FilterInvocation> webExpressionHandler() {
        DefaultWebSecurityExpressionHandler defaultWebSecurityExpressionHandler = new DefaultWebSecurityExpressionHandler();
        defaultWebSecurityExpressionHandler.setRoleHierarchy(roleHierarchy());
        return defaultWebSecurityExpressionHandler;
    }

}


/**
 * https://github.com/spring-projects/spring-security-samples/blob/5.7.x/servlet/xml/java/contacts/src/main/java/sample/contact/ContactManagerBackend.java
 * https://github.com/spring-projects/spring-security-samples
 * https://www.waitingforcode.com/spring-security/introduction-to-spring-security/read
 * https://www.baeldung.com/spring-security-acl#:~:text=Spring%20Security%20Access%20Control%20List,the%20typical%20per%2Doperation%20level.
 */