package com.home.board.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.home.board.infrastructure.security.CustomLogoutHandler;
import com.home.board.infrastructure.security.CustomOAuth2UserService;
import com.home.board.infrastructure.security.JwtAuthenticationFilter;
import com.home.board.infrastructure.security.OAuth2AuthenticationFailureHandler;
import com.home.board.infrastructure.security.OAuth2AuthenticationSuccessHandler;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {


    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    private final CustomLogoutHandler customLogoutHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. CSRF 비활성화 (람다식 사용)
            .csrf(AbstractHttpConfigurer::disable)
            
            // 2. 세션 관리: STATELESS 설정
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            
            // 3. 인가 정책 (authorizeRequests -> authorizeHttpRequests)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/").authenticated() // 루트 경로는 인증 필요
                .requestMatchers("/login", "/oauth2/**", "/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll() // 화이트리스트
                .anyRequest().authenticated() // 나머지는 모두 인증 필요
            )
            
            // 4. OAuth2 로그인 설정
            .oauth2Login(oauth -> oauth
                .loginPage("/login")
                .userInfoEndpoint(userInfo -> userInfo
                    .userService(customOAuth2UserService) // 사용자 서비스 설정
                )
                .successHandler(oAuth2AuthenticationSuccessHandler)
                .failureHandler(oAuth2AuthenticationFailureHandler)
            )
            
            // 5. 로그아웃 설정
            .logout(logout -> logout
                .logoutUrl("/logout")
                .addLogoutHandler(customLogoutHandler)
                .logoutSuccessUrl("/")
            )
            
            // 6. 예외 처리 (인증 실패 시 엔트리 포인트)
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))
            )
            
            // 7. JWT 필터 추가
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    
    
    /*
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    	http
	        // CSRF 비활성화 (REST API 용도)
	        .csrf(csrf -> csrf.disable())
//	        .cors(cors -> {}) // Spring MVC의 CorsConfigurationSource를 따르게 함
	
	     // 세션은 STATELESS (JWT 사용)
	        .sessionManagement(session ->
	                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	
	        // 기본 로그인/로그아웃/Basic 인증 비활성화
	        //.formLogin(form -> form.disable())
	        //.httpBasic(basic -> basic.disable())
	        //.logout(logout -> logout.disable())
	
	        // 경로별 인가 정책
	        .authorizeHttpRequests(auth -> auth
	                // Preflight 요청 허용
//	                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
	                // 추가정보 입력은 공개
	                .requestMatchers("/").authenticated()
	                // 홈/기타 공개 리소스 허용
	                .requestMatchers("/login", "/oauth2/**", "/css/**", "/js/**", "/images/**", "/favicon.ico", "/main").permitAll()
	                // 나머지는 인증 필요
	                .anyRequest().authenticated()
	        )
	        
	        // OAuth2 로그인 → custom successHandler 사용
	        .oauth2Login(oauth -> oauth
	        		.loginPage("/login")
	        		.userInfoEndpoint(user -> user.userService(customOAuth2UserService))
	        		.successHandler(oAuth2AuthenticationSuccessHandler)
	        		.failureHandler(oAuth2AuthenticationFailureHandler)
    		)
	        
	
	        
	        .logout(logout -> logout
					// 해당 api로 요청 시 로그아웃을 수행한다(컨트롤러 내부 메서드는 수행 x, url만 연결)
					.logoutUrl("/logout")
					// 여기에서 본격적인 logout 과정 수행(jwt 파기, 쿠키 삭제)
					.addLogoutHandler(customLogoutHandler)
					.logoutSuccessUrl("/")
					// 세션 무효화, authentication 제거
					.invalidateHttpSession(true)
					.clearAuthentication(true)
			)
	        
	        // 인증 실패 시 → /login 으로 이동
	        .exceptionHandling(ex -> ex
	                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))
	        )
	
	        // JwtAuthenticationFilter 등록 (UsernamePasswordAuthenticationFilter 앞에)
	        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }
    */
    
}