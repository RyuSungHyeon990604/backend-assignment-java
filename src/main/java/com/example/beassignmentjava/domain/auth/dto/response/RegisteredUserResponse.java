package com.example.beassignmentjava.domain.auth.dto.response;

import com.example.beassignmentjava.domain.auth.dto.RoleDto;
import com.example.beassignmentjava.domain.auth.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RegisteredUserResponse {

	@Schema(description = "사용자 이름", example = "Ryu")
	private String username;

	@Schema(description = "사용자 별명", example = "nickname")
	private String nickname;

	@Schema(description = "사용자 권한 목록", example = "[{\"role\": \"ROLE_USER\"}]")
	private List<RoleDto> roles;


	public static RegisteredUserResponse of(User user) {
		List<RoleDto> roles = user.getRoles().stream().map(RoleDto::of).toList();
		return new RegisteredUserResponse(user.getUsername(), user.getNickName(), roles);
	}
}
