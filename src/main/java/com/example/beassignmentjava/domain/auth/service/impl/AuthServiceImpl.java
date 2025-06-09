package com.example.beassignmentjava.domain.auth.service.impl;

import com.example.beassignmentjava.domain.auth.dto.request.LoginRequest;
import com.example.beassignmentjava.domain.auth.dto.request.SignUpRequest;
import com.example.beassignmentjava.domain.auth.dto.response.LoginResponse;
import com.example.beassignmentjava.domain.auth.dto.response.SignUpResponse;
import com.example.beassignmentjava.domain.auth.entity.User;
import com.example.beassignmentjava.domain.auth.enums.UserRole;
import com.example.beassignmentjava.domain.auth.repository.UserRepository;
import com.example.beassignmentjava.domain.auth.service.AuthService;
import com.example.beassignmentjava.exception.ApplicationException;
import com.example.beassignmentjava.exception.ErrorCode;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

	private final UserRepository userRepository;

	public AuthServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public SignUpResponse signUp(SignUpRequest signUpRequest) {

		if(userRepository.existsByUsername(signUpRequest.getUsername())) {
			throw new ApplicationException(ErrorCode.USER_ALREADY_EXISTS);
		}

		User user = User.builder()
			.username(signUpRequest.getUsername())
			.password(signUpRequest.getPassword())
			.nickName(signUpRequest.getNickname())
			.role(UserRole.ROLE_USER)
			.build();

		User savedUser = userRepository.save(user);

		return SignUpResponse.of(savedUser);
	}

	@Override
	public LoginResponse login(LoginRequest loginRequest) {
		User user = userRepository.findByUsername(loginRequest.getUsername())
			.orElseThrow(()->new ApplicationException(ErrorCode.INVALID_CREDENTIALS));

		if(!user.getPassword().equals(loginRequest.getPassword())) {
			throw new ApplicationException(ErrorCode.INVALID_CREDENTIALS);
		}

		return new LoginResponse("example token");
	}

	@Override
	public SignUpResponse grantAdminRole(Long userId) {
		User user = userRepository.findById(userId)
			.orElseThrow(()->new ApplicationException(ErrorCode.INVALID_USER));
		user.grantAdminRole();
		userRepository.save(user);

		return SignUpResponse.of(user);
	}
}
