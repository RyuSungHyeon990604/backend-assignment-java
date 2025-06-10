package com.example.beassignmentjava.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginRequest {

	@Schema(description = "사용자 이름", example = "Ryu")
	private String username;

	@Schema(description = "비밀번호", example = "1234")
	private String password;
}
