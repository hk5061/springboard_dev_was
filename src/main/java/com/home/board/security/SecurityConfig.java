package com.home.board.security;

import com.home.board.config.AuditorAwareConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // Spring Security 설정 활성화
public class SecurityConfig {

    private final AuditorAwareConfig auditorAwareConfig;

    SecurityConfig(AuditorAwareConfig auditorAwareConfig) {
        this.auditorAwareConfig = auditorAwareConfig;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    	/**
         * authenticated() : 인증 필요
         * permitAll() : 인증 필요 없음
         */
    	http.authorizeHttpRequests((requests) -> {
            requests.anyRequest().permitAll();
        }).formLogin(Customizer.withDefaults())
        .httpBasic(Customizer.withDefaults());
        
        return http.build();
    	
    	/*
        return http.authorizeRequests()
                .mvcMatchers("/", "/members/new", "/favicon.ico", "/error", "/css/**", "/js/**", "/image/**", "/api/comments").permitAll()
                .mvcMatchers(HttpMethod.PATCH, "/api/childs").permitAll()
                .mvcMatchers(HttpMethod.GET, "/posts", "/posts/*", "/api/**").permitAll()
                .mvcMatchers(HttpMethod.POST, "/posts").hasAnyRole("ADMIN", "MANAGER", "USER") // hasAnyRole과 hasRole 함수는 자동으로 "ROLE_"이 붙음.
                .anyRequest().authenticated() // 위 요청 외에는 모두 로그인 해야함
                .and()
                .formLogin()
                .and()
                .logout().logoutSuccessUrl("/")
                .and()
                .csrf().disable() // csrf 비활성화
                .build();
                */
    }
}