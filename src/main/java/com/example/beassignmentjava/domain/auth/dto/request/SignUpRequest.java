package com.example.beassignmentjava.domain.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignUpRequest {
	private String username;
	private String password;
	private String nickname;
}
