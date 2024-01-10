package com.example.springsecurityclient.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

////@EnableWebSecurity annotation implicitly includes the @Configuration annotation
//@Configuration can be used in any Spring application for various purposes. In contrast,
//@EnableWebSecurity is used only in the context of a web application where Spring Security is implemented.
@EnableWebSecurity
public class WebSecurityConfig {
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
