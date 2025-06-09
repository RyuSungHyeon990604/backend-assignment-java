package com.example.beassignmentjava.config.security;

import com.example.beassignmentjava.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final CustomAccessDeniedHandler customAccessDeniedHandler;
	private final CustomeUnauthorizedHandler customeUnauthorizedHandler;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		//jwt 와 같이 사용중이므로 spring security에서는 인가와 관련된 작업만 설정

		http.csrf(AbstractHttpConfigurer::disable) //폼 기반 로그인 방식이 아니라면 보통 CSRF를 비활성화함
			.formLogin(AbstractHttpConfigurer::disable)//로그인은 따로 구현된 api로 처리
			.anonymous(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable)
			.logout(AbstractHttpConfigurer::disable)
			.rememberMe(AbstractHttpConfigurer::disable)
			.addFilterBefore(jwtAuthenticationFilter, SecurityContextHolderAwareRequestFilter.class)
			//인가 설정
			.authorizeHttpRequests(authorize -> authorize
				.requestMatchers("/login", "/signup").permitAll()//회원가입과 로그인은 모두 허용
				.requestMatchers("/admin/**").hasRole("ADMIN")
				.anyRequest().authenticated()//나머지는 인증된 사용자만, 세부 인가처리는 @Secured 로 처리
			)
			.exceptionHandling(
				exception ->
					exception.accessDeniedHandler(customAccessDeniedHandler)
					.authenticationEntryPoint(customeUnauthorizedHandler)
			)
			//세션은 사용하지않도록 설정
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		return http.build();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
