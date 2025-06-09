package com.example.beassignmentjava.domain.auth.service;

import com.example.beassignmentjava.domain.auth.dto.request.LoginRequest;
import com.example.beassignmentjava.domain.auth.dto.request.SignUpRequest;
import com.example.beassignmentjava.domain.auth.dto.response.LoginResponse;
import com.example.beassignmentjava.domain.auth.dto.response.SignUpResponse;

public interface AuthService {
	SignUpResponse signUp(SignUpRequest signUpRequest);
	LoginResponse login(LoginRequest loginRequest);
	SignUpResponse grantAdminRole(Long userId);
}
