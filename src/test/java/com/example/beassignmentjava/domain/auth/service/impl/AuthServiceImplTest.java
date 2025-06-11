package com.example.beassignmentjava.domain.auth.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.example.beassignmentjava.domain.auth.dto.request.LoginRequest;
import com.example.beassignmentjava.domain.auth.dto.request.SignUpRequest;
import com.example.beassignmentjava.domain.auth.dto.response.JwtResponse;
import com.example.beassignmentjava.domain.auth.dto.response.RegisteredUserResponse;
import com.example.beassignmentjava.domain.auth.entity.User;
import com.example.beassignmentjava.domain.auth.enums.UserRole;
import com.example.beassignmentjava.domain.auth.repository.UserRepository;
import com.example.beassignmentjava.exception.ApplicationException;
import com.example.beassignmentjava.exception.ErrorCode;
import com.example.beassignmentjava.util.JwtUtil;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

	@Mock
	private UserRepository userRepository;
	@Mock
	private BCryptPasswordEncoder passwordEncoder;
	@Mock
	private JwtUtil jwtUtil;

	@InjectMocks
	private AuthServiceImpl authService;

	@Test
	@DisplayName("회원가입 성공")
	void signUp_success() {
		//given
		SignUpRequest request = new SignUpRequest("testuser", "pass", "닉네임");
		given(userRepository.existsByUsername(anyString())).willReturn(false);
		given(passwordEncoder.encode(anyString())).willReturn("encodedPass");
		User user = User.builder().username("testuser").password("encodedPass").nickName("닉네임").role(UserRole.ROLE_USER).build();
		given(userRepository.save(any(User.class))).willReturn(user);

		//when
		RegisteredUserResponse response = authService.signUp(request);

		//then
		assertEquals("testuser", response.getUsername());
	}

	@Test
	@DisplayName("회원가입 실패 - 중복 사용자")
	void signUp_duplicate() {
		//given
		given(userRepository.existsByUsername(anyString())).willReturn(true);
		SignUpRequest request = new SignUpRequest("dupuser", "pw", "닉네임");

		//when && then
		ApplicationException ex = assertThrows(ApplicationException.class, () -> authService.signUp(request));
		assertEquals(ErrorCode.USER_ALREADY_EXISTS, ex.getError());
	}

	@Test
	@DisplayName("로그인 성공")
	void login_success() {
		//given
		LoginRequest request = new LoginRequest("testuser", "pass");
		User user = User.builder().username("testuser").password("encodedPass").nickName("닉네임").role(UserRole.ROLE_USER).build();
		user.assignId(1L);
		given(userRepository.findByUsername(anyString())).willReturn(Optional.of(user));
		given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);
		given(jwtUtil.createToken(anyLong(), anyString(), anyString(), anyList())).willReturn("jwt-token");

		//when
		JwtResponse response = authService.login(request);

		//then
		assertEquals("jwt-token", response.getToken());
	}

	@Test
	@DisplayName("로그인 실패 - 비밀번호 불일치")
	void login_wrongPassword() {
		//given
		LoginRequest request = new LoginRequest("testuser", "wrong");
		User user = User.builder().username("testuser").password("encodedPass").nickName("닉네임").role(UserRole.ROLE_USER).build();
		given(userRepository.findByUsername(anyString())).willReturn(Optional.of(user));
		given(passwordEncoder.matches(anyString(), anyString())).willReturn(false);

		//when && then
		ApplicationException ex = assertThrows(ApplicationException.class, () -> authService.login(request));
		assertEquals(ErrorCode.INVALID_CREDENTIALS, ex.getError());
	}

	@Test
	@DisplayName("로그인 실패 - 사용자 없음")
	void login_userNotFound() {
		//given
		given(userRepository.findByUsername(anyString())).willReturn(Optional.empty());
		LoginRequest request = new LoginRequest("unknown", "pw");

		//when && then
		ApplicationException ex = assertThrows(ApplicationException.class, () -> authService.login(request));
		assertEquals(ErrorCode.INVALID_CREDENTIALS, ex.getError());
	}

	@Test
	@DisplayName("관리자 권한 부여 성공")
	void grantAdmin_success() {
		//given
		User user = User.builder().username("user").password("pw").nickName("닉").role(UserRole.ROLE_USER).build();
		user.assignId(1L);
		given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
		given(userRepository.save(any(User.class))).willReturn(user);

		//when
		RegisteredUserResponse response = authService.grantAdminRole(1L);

		//then
		assertEquals(UserRole.ROLE_ADMIN, response.getRoles().get(0).getRole());
	}

	@Test
	@DisplayName("관리자 권한 부여 실패 - 유저 없음")
	void grantAdmin_userNotFound() {
		//given
		given(userRepository.findById(anyLong())).willReturn(Optional.empty());

		//when && then
		ApplicationException ex = assertThrows(ApplicationException.class, () -> authService.grantAdminRole(999L));
		assertEquals(ErrorCode.INVALID_USER, ex.getError());
	}
}