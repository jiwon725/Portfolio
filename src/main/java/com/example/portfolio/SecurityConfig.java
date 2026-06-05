package com.example.portfolio;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable) // 포트폴리오 프로젝트의 간소화를 위해 CSRF 비활성화
            .authorizeHttpRequests((authorize) -> authorize
                // 정적 자원 및 공개 페이지
                .requestMatchers("/", "/detail/{id}", "/api/portfolio", "/style.css", "/script.js", "/h2-console/**").permitAll()
                // 그 외 모든 요청(등록/수정 폼, 수정/삭제 API)은 인증 필요
                .anyRequest().authenticated()
            )
            .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable())) // H2 콘솔 사용을 위해 X-Frame-Options 비활성화
            .formLogin(form -> form
                .loginPage("/login")         // 커스텀 로그인 페이지 경로
                .defaultSuccessUrl("/")      // 로그인 성공 시 메인 화면으로 이동
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")       // 로그아웃 성공 시 메인 화면으로 이동
                .invalidateHttpSession(true)
            );

        return http.build();
    }
}
