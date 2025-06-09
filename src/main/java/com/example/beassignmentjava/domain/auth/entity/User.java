package com.example.beassignmentjava.domain.auth.entity;

import com.example.beassignmentjava.domain.auth.enums.UserRole;
import com.example.beassignmentjava.exception.ApplicationException;
import com.example.beassignmentjava.exception.ErrorCode;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class User {
	private Long id;
	private String username;
	private String password;
	private String nickName;
	private List<UserRole> roles = new ArrayList<>();

	@Builder
	public User(String username, String password, String nickName, UserRole role) {
		this.username = username;
		this.password = password;
		this.nickName = nickName;
		this.roles.add(role);
	}

	public void grantAdminRole() {
		this.roles.set(0, UserRole.ROLE_ADMIN);
	}

	public void assignId(Long id) {
		if(this.id != null) {
			throw new ApplicationException(ErrorCode.ID_ALREADY_ASSIGNED);
		}
		this.id = id;
	}
}
