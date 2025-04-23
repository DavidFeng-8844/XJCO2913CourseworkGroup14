package com.example.Scootify.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .headers().frameOptions().disable() // 允许 H2 控制台的 iframe 嵌入
            .and() // Enable H2 console access
            .csrf().disable()
            .authorizeRequests()
            .requestMatchers("/h2-console/**").permitAll() // 允许访问 H2 控制台
            .requestMatchers("/api/auth/**").permitAll()  // 允许注册和登录
            .anyRequest().authenticated();
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}