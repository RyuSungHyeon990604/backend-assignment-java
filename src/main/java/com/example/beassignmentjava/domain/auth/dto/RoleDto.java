package com.example.beassignmentjava.domain.auth.dto;

import com.example.beassignmentjava.domain.auth.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RoleDto {
	private UserRole role;

	public static RoleDto of(UserRole role) {
		return new RoleDto(role);
	}
}
