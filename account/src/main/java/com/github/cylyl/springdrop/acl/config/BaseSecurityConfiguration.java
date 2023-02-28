package com.github.cylyl.springdrop.acl.config;

import com.github.cylyl.springdrop.acl.PermissionsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

import javax.sql.DataSource;

public abstract class BaseSecurityConfiguration {
    @Autowired
    PermissionsManager permissionsManager;

    /**
     * JDBC Authentication
     * Below is an example configuration using the WebSecurityConfigurerAdapter with an embedded DataSource that is initialized with the default schema and has a single user:
     */
//    @Configuration
//    public class SecurityConfiguration {
//        @Bean
//        public DataSource dataSource() {
//            return new EmbeddedDatabaseBuilder()
//                    .setType(EmbeddedDatabaseType.H2)
//                    .addScript(JdbcDaoImpl.DEFAULT_USER_SCHEMA_DDL_LOCATION)
//                    .build();
//        }
//
    @Bean
    public UserDetailsManager users(DataSource dataSource) {
        JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
        permissionsManager.setPasswordEncoder(passwordEncoder());
        permissionsManager.setUserDetailsManager(users);
        return users;
    }
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
