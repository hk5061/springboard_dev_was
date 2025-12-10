package com.home.board.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	
//	@Bean
//    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity
//                .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
//                        .requestMatchers("/**").permitAll())
//                .csrf((csrf) -> csrf
//                        .ignoringRequestMatchers("/members/**"))
//                .headers((headers) -> headers
//                        .addHeaderWriter(new XFrameOptionsHeaderWriter(
//                                XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN
//                        )))
//                .formLogin((formLogin) -> formLogin
//                        .loginPage("/user/login")
//                        .defaultSuccessUrl("/"))
//                .logout((logout) -> logout
//                // 문제 코드
////	                        .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
//                		.logoutRequestMatcher(new RequestMatcher() {
//                		    @Override
//                		    public boolean matches (HttpServletRequest request){
//                		        return "GET".equalsIgnoreCase(request.getMethod())
//                		                && request.getRequestURI().equals("/user/logout");
//                		    }
//                		})
//                        .logoutSuccessUrl("/")
//                        .invalidateHttpSession(true));
//	        return httpSecurity.build();
//	    }
	
	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. CSRF 비활성화 (Lambda DSL)
            .csrf(AbstractHttpConfigurer::disable)

            // 2. HTTP 요청 인가 설정 (authorizeRequests -> authorizeHttpRequests)
            .authorizeHttpRequests(auth -> auth
                // mvcMatchers -> requestMatchers 변경
                .requestMatchers("/", "/members/new", "/favicon.ico", "/error", "/css/**", "/js/**", "/image/**", "/api/comments").permitAll()
                .requestMatchers(HttpMethod.PATCH, "/api/childs").permitAll()
                .requestMatchers(HttpMethod.GET, "/posts", "/posts/*", "/api/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/posts").hasAnyRole("ADMIN", "MANAGER", "USER")
                .anyRequest().authenticated()
            )

            // 3. Form 로그인 설정
            .formLogin(login -> login
                .permitAll() // 기본 로그인 페이지 접근 허용
            )

            // 4. 로그아웃 설정
            .logout(logout -> logout
                .logoutSuccessUrl("/")
                .permitAll()
            );

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
	
	
}