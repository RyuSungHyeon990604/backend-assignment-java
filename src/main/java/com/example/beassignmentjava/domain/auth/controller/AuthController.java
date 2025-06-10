package com.example.beassignmentjava.domain.auth.controller;

import com.example.beassignmentjava.domain.auth.dto.request.LoginRequest;
import com.example.beassignmentjava.domain.auth.dto.request.SignUpRequest;
import com.example.beassignmentjava.domain.auth.dto.response.LoginResponse;
import com.example.beassignmentjava.domain.auth.dto.response.SignUpResponse;
import com.example.beassignmentjava.domain.auth.service.AuthService;
import com.example.beassignmentjava.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;

	@Operation(summary = "회원가입", description = "신규 유저를 생성합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "회원가입 성공"),
		@ApiResponse(responseCode = "400", description = "이미 존재하는 사용자",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	})
	@PostMapping("/signup")
	private ResponseEntity<SignUpResponse> signUp(@RequestBody SignUpRequest signUpRequest) {
		return ResponseEntity.status(HttpStatus.CREATED).body(authService.signUp(signUpRequest));
	}

	@Operation(summary = "로그인", description = "JWT 토큰을 발급받습니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "로그인 성공"),
		@ApiResponse(responseCode = "401", description = "아이디 또는 비밀번호 불일치",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	})
	@PostMapping("/login")
	private ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
		return ResponseEntity.ok(authService.login(loginRequest));
	}

	@Operation(
		summary = "관리자 권한 부여",
		description = "특정 사용자에게 관리자 권한을 부여합니다.",
		security = @SecurityRequirement(name = "jwt")
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "관리자 권한 부여 성공"),
		@ApiResponse(responseCode = "404", description = "존재하지 않는 사용자",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "403", description = "관리자 권한 없음",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	})
	@PatchMapping("/admin/users/{userId}/roles")
	private ResponseEntity<SignUpResponse> grantAdminRole(@PathVariable Long userId) {
		return ResponseEntity.ok(authService.grantAdminRole(userId));
	}
}
