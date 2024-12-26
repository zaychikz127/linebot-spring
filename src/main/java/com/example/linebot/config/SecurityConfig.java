package com.example.linebot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @SuppressWarnings({ "deprecation", "removal" })
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable() // ปิด CSRF หากไม่ได้ใช้งาน
            .authorizeRequests()
                .anyRequest().permitAll() // อนุญาตทุก Endpoint โดยไม่ต้อง Auth
            .and()
            .httpBasic().disable(); // ปิด Basic Auth
        return http.build();
    }
}
