package Scootify.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // 直接用方法引用
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**").permitAll() // 允许访问注册接口
                .anyRequest().authenticated()); // 其他请求需要认证
        return http.build();
    }
}