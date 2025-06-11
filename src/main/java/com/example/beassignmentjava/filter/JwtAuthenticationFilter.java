package com.example.beassignmentjava.filter;

import com.example.beassignmentjava.config.security.AuthUser;
import com.example.beassignmentjava.config.security.JwtAuthenticationToken;
import com.example.beassignmentjava.domain.auth.enums.UserRole;
import com.example.beassignmentjava.exception.ErrorCode;
import com.example.beassignmentjava.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private final JwtUtil jwtUtil;
	private final static String TOKEN_PREFIX = "Bearer ";

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String authorizationHeader = request.getHeader("Authorization");

		if (authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PREFIX)) {
			String token = authorizationHeader.substring(TOKEN_PREFIX.length());
			try {
				Claims claims = jwtUtil.extractClaims(token);

				if (SecurityContextHolder.getContext().getAuthentication() == null) {
					setAuthentication(claims);
				}
			} catch (JwtException e) {
				sendError(response, HttpServletResponse.SC_UNAUTHORIZED, "ACCESS_DENIED", ErrorCode.INVALID_TOKEN.getMessage());
				return;
			}
		}
		filterChain.doFilter(request, response);
	}

	private void setAuthentication(Claims claims) {
		Long userId = Long.valueOf(claims.getSubject());
		String username = claims.get("username", String.class);
		List<String> roleNames = claims.get("roles", List.class);
		List<UserRole> rolse = roleNames.stream()
			.map(UserRole::valueOf).toList();

		AuthUser authUser = new AuthUser(userId, username, rolse);
		JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(authUser);
		SecurityContextHolder.getContext().setAuthentication(authenticationToken);
	}

	private void sendError(HttpServletResponse response, int status, String code, String message) throws IOException {
		response.setStatus(status);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		Map<String, Object> body = new HashMap<>();
		Map<String, Object> error = new HashMap<>();
		error.put("code", code);
		error.put("message", message);
		body.put("error", error);

		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(body);
		response.getWriter().write(json);
	}
}
