package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Spring Security 설정을 담당하는 클래스입니다.
 * JWT 기반의 인증 및 권한 부여를 설정합니다.
 */
@Configuration
@EnableWebSecurity
// `@PreAuthorize` 어노테이션을 컨트롤러 메소드에서 사용할 수 있도록 활성화합니다.
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    /**
     * HTTP 보안 설정을 구성하는 Bean을 생성합니다.
     * @param http HttpSecurity 객체
     * @return 빌드된 SecurityFilterChain
     * @throws Exception 설정 중 발생할 수 있는 예외
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CORS 설정을 기본값으로 활성화합니다. (아래 corsConfigurationSource Bean 사용)
                .cors(Customizer.withDefaults())
                // REST API 서버는 stateless하게 동작하므로 CSRF 보호를 비활성화합니다.
                .csrf(csrf -> csrf.disable())
                // 세션을 사용하지 않고, 각 요청을 독립적으로 처리하도록 설정합니다. (STATELESS)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 모든 HTTP 요청에 대해 인증을 요구하도록 설정합니다.
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().authenticated()
                )
                // OAuth2 리소스 서버 설정을 활성화하고, JWT를 사용하도록 지정합니다.
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

        return http.build();
    }

    /**
     * CORS(Cross-Origin Resource Sharing) 설정을 구성하는 Bean을 생성합니다.
     * 프론트엔드 애플리케이션과의 통신을 허용하기 위해 필요합니다.
     * @return CorsConfigurationSource 객체
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // [중요] 프론트엔드 개발 서버의 실제 주소로 변경해야 합니다.
        // 예: Vue(Vite) 기본 포트 5173, React 기본 포트 3000
        configuration.setAllowedOrigins(List.of("http://localhost:5173", "http://localhost:3000"));
        // 허용할 HTTP 메소드를 지정합니다.
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // 모든 HTTP 헤더를 허용합니다.
        configuration.setAllowedHeaders(List.of("*"));
        // 자격 증명(쿠키 등)을 허용합니다.
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 모든 경로("/**")에 대해 위에서 정의한 CORS 설정을 적용합니다.
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
