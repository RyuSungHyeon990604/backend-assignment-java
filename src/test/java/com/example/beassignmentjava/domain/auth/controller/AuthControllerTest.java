package com.example.beassignmentjava.domain.auth.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.beassignmentjava.domain.auth.dto.RoleDto;
import com.example.beassignmentjava.domain.auth.dto.request.LoginRequest;
import com.example.beassignmentjava.domain.auth.dto.request.SignUpRequest;
import com.example.beassignmentjava.domain.auth.dto.response.JwtResponse;
import com.example.beassignmentjava.domain.auth.dto.response.RegisteredUserResponse;
import com.example.beassignmentjava.domain.auth.enums.UserRole;
import com.example.beassignmentjava.domain.auth.service.AuthService;
import com.example.beassignmentjava.exception.ApplicationException;
import com.example.beassignmentjava.exception.ErrorCode;
import com.example.beassignmentjava.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private JwtUtil jwtUtil;

	@MockitoBean
	private AuthService authService;

	@Test
	@DisplayName("회원가입 성공")
	void signUp_success() throws Exception {
		// given
		SignUpRequest request = new SignUpRequest("ryu", "1234", "류성현");
		given(authService.signUp(any())).willReturn(new RegisteredUserResponse("ryu", "1234", null));

		// when & then
		mockMvc.perform(post("/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.username").value("ryu"));
	}

	@Test
	@DisplayName("회원가입 실패 - 중복 사용자")
	void signUp_userAlreadyExists() throws Exception {
		// given
		SignUpRequest request = new SignUpRequest("ryu", "1234", "류성현");
		given(authService.signUp(any())).willThrow(new ApplicationException(ErrorCode.USER_ALREADY_EXISTS));

		// when & then
		mockMvc.perform(post("/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.error.message").value("이미 가입된 사용자입니다."));
	}

	@Test
	@DisplayName("로그인 성공")
	void login_success() throws Exception {
		// given
		LoginRequest request = new LoginRequest("ryu", "1234");
		given(authService.login(any())).willReturn(new JwtResponse("Bearer token123"));

		// when & then
		mockMvc.perform(post("/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.token").value("Bearer token123"));
	}

	@Test
	@DisplayName("로그인 실패 - 비밀번호 불일치")
	void login_fail_wrongPassword() throws Exception {
		// given
		LoginRequest request = new LoginRequest("ryu", "wrongpw");
		given(authService.login(any())).willThrow(new ApplicationException(ErrorCode.INVALID_CREDENTIALS));

		// when & then
		mockMvc.perform(post("/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isUnauthorized())
			.andExpect(jsonPath("$.error.message").value("아이디 또는 비밀번호가 올바르지 않습니다."));
	}

	@Test
	@DisplayName("관리자 권한 부여 성공")
	void grantAdminRole_success() throws Exception {
		// given
		Long userId = 1L;
		String token = jwtUtil.createToken(1L, "admin", null, List.of(UserRole.ROLE_ADMIN));

		given(authService.grantAdminRole(userId))
			.willReturn(new RegisteredUserResponse("admin", "1234", List.of(new RoleDto(UserRole.ROLE_ADMIN))));

		// when & then
		mockMvc.perform(patch("/admin/users/{userId}/roles", userId)
				.header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.username").value("admin"));
	}

	@Test
	@DisplayName("관리자 권한 부여 실패 - 유효하지 않은 유저")
	void grantAdminRole_invalidUser() throws Exception {
		// given
		Long userId = 999L;
		String token = jwtUtil.createToken(1L, "admin", null, List.of(UserRole.ROLE_ADMIN));

		given(authService.grantAdminRole(userId))
			.willThrow(new ApplicationException(ErrorCode.INVALID_USER));

		// when & then
		mockMvc.perform(patch("/admin/users/{userId}/roles", userId)
				.header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.error.message").value("유효하지 않은 유저입니다."));
	}

}
