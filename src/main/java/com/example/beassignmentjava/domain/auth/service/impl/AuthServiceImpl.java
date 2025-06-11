package com.example.beassignmentjava.domain.auth.service.impl;

import com.example.beassignmentjava.domain.auth.dto.request.LoginRequest;
import com.example.beassignmentjava.domain.auth.dto.request.SignUpRequest;
import com.example.beassignmentjava.domain.auth.dto.response.JwtResponse;
import com.example.beassignmentjava.domain.auth.dto.response.RegisteredUserResponse;
import com.example.beassignmentjava.domain.auth.entity.User;
import com.example.beassignmentjava.domain.auth.enums.UserRole;
import com.example.beassignmentjava.domain.auth.repository.UserRepository;
import com.example.beassignmentjava.domain.auth.service.AuthService;
import com.example.beassignmentjava.exception.ApplicationException;
import com.example.beassignmentjava.exception.ErrorCode;
import com.example.beassignmentjava.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;

	@Override
	public RegisteredUserResponse signUp(SignUpRequest signUpRequest) {

		if(userRepository.existsByUsername(signUpRequest.getUsername())) {
			throw new ApplicationException(ErrorCode.USER_ALREADY_EXISTS);
		}

		User user = User.builder()
			.username(signUpRequest.getUsername())
			.password(passwordEncoder.encode(signUpRequest.getPassword()))
			.nickName(signUpRequest.getNickname())
			.role(UserRole.ROLE_USER)
			.build();

		User savedUser = userRepository.save(user);

		return RegisteredUserResponse.of(savedUser);
	}

	@Override
	public JwtResponse login(LoginRequest loginRequest) {
		User user = userRepository.findByUsername(loginRequest.getUsername())
			.orElseThrow(()->new ApplicationException(ErrorCode.INVALID_CREDENTIALS));

		if(!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
			throw new ApplicationException(ErrorCode.INVALID_CREDENTIALS);
		}
		String jwtToken = jwtUtil.createToken(user.getId(), user.getUsername(), user.getNickName(), user.getRoles());
		return new JwtResponse(jwtToken);
	}

	@Override
	public RegisteredUserResponse grantAdminRole(Long userId) {
		User user = userRepository.findById(userId)
			.orElseThrow(()->new ApplicationException(ErrorCode.INVALID_USER));
		user.grantAdminRole();
		userRepository.save(user);

		return RegisteredUserResponse.of(user);
	}
}
