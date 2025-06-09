package com.example.beassignmentjava.config.security;

import com.example.beassignmentjava.domain.auth.enums.UserRole;
import java.util.Collection;
import java.util.List;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Getter
public class AuthUser {
	private Long userId;
	private String username;
	private Collection<? extends GrantedAuthority> authorities;

	public AuthUser(Long userId, String username, List<UserRole> roles) {
		this.userId = userId;
		this.username = username;
		this.authorities = roles.stream().map(role -> new SimpleGrantedAuthority(role.name())).toList();
	}
}
