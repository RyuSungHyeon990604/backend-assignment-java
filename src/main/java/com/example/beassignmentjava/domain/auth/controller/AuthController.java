package com.example.beassignmentjava.domain.auth.controller;

import com.example.beassignmentjava.domain.auth.dto.request.LoginRequest;
import com.example.beassignmentjava.domain.auth.dto.request.SignUpRequest;
import com.example.beassignmentjava.domain.auth.dto.response.LoginResponse;
import com.example.beassignmentjava.domain.auth.dto.response.SignUpResponse;
import com.example.beassignmentjava.domain.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;

	@PostMapping("/signup")
	private ResponseEntity<SignUpResponse> signUp(@RequestBody SignUpRequest signUpRequest) {
		return ResponseEntity.status(HttpStatus.CREATED).body(authService.signUp(signUpRequest));
	}

	@PostMapping("/login")
	private ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
		return ResponseEntity.ok(authService.login(loginRequest));
	}

	@PatchMapping("/admin/users/{userId}/roles")
	private ResponseEntity<SignUpResponse> grantAdminRole(@PathVariable Long userId) {
		return ResponseEntity.ok(authService.grantAdminRole(userId));
	}
}
