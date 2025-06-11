package com.example.beassignmentjava.domain.auth.service;

import com.example.beassignmentjava.domain.auth.dto.request.LoginRequest;
import com.example.beassignmentjava.domain.auth.dto.request.SignUpRequest;
import com.example.beassignmentjava.domain.auth.dto.response.JwtResponse;
import com.example.beassignmentjava.domain.auth.dto.response.RegisteredUserResponse;

public interface AuthService {
	RegisteredUserResponse signUp(SignUpRequest signUpRequest);
	JwtResponse login(LoginRequest loginRequest);
	RegisteredUserResponse grantAdminRole(Long userId);
}
