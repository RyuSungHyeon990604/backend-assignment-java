package com.example.beassignmentjava.domain.auth.dto.response;

import com.example.beassignmentjava.domain.auth.dto.RoleDto;
import com.example.beassignmentjava.domain.auth.entity.User;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignUpResponse {
	private String username;
	private String password;
	private List<RoleDto> roles;
	public static SignUpResponse of(User user) {
		List<RoleDto> roles = user.getRoles().stream().map(RoleDto::of).toList();
		return new SignUpResponse(user.getUsername(), user.getPassword(), roles);
	}
}
