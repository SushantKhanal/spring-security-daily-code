package com.example.springsecurityclient.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

////@EnableWebSecurity annotation implicitly includes the @Configuration annotation
//@Configuration can be used in any Spring application for various purposes. In contrast,
//@EnableWebSecurity is used only in the context of a web application where Spring Security is implemented.
@EnableWebSecurity
@Configuration
public class WebSecurityConfig {
    private static final String[] WHITE_LIST_URLS = {
            "/hello",
            "/register"
    };
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers(WHITE_LIST_URLS).permitAll()
                );
        return httpSecurity.build();
    }
}
