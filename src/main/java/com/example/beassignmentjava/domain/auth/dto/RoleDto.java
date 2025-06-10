package com.example.beassignmentjava.domain.auth.dto;

import com.example.beassignmentjava.domain.auth.enums.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RoleDto {

	@Schema(description = "권한 이름", example = "ROLE_USER")
	private UserRole role;

	public static RoleDto of(UserRole role) {
		return new RoleDto(role);
	}
}
