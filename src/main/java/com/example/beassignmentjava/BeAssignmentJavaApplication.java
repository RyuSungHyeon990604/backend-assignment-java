package com.example.beassignmentjava;

import com.example.beassignmentjava.domain.auth.dto.request.SignUpRequest;
import com.example.beassignmentjava.domain.auth.service.AuthService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BeAssignmentJavaApplication {

	public static void main(String[] args) {
		SpringApplication.run(BeAssignmentJavaApplication.class, args);
	}


	//기본 관리자계정 생성
	@Bean
	CommandLineRunner commandLineRunner(AuthService authService) {
		return args -> {
			SignUpRequest signUpRequest = new SignUpRequest("admin", "admin", "admin");
			authService.signUp(signUpRequest);
			authService.grantAdminRole(1L);
		};
	}
}
