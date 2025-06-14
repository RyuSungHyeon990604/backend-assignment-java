package com.example.beassignmentjava.filter;

import com.example.beassignmentjava.config.security.AuthUser;
import com.example.beassignmentjava.config.security.JwtAuthenticationToken;
import com.example.beassignmentjava.domain.auth.enums.UserRole;
import com.example.beassignmentjava.exception.ErrorCode;
import com.example.beassignmentjava.exception.ErrorResponse;
import com.example.beassignmentjava.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
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
				sendError(response, ErrorCode.INVALID_TOKEN);
				return;
			}
		}
		filterChain.doFilter(request, response);
	}

	private void setAuthentication(Claims claims) {
		Long userId = Long.valueOf(claims.getSubject());
		String username = claims.get("username", String.class);
		List<?> roleNames = claims.get("roles", List.class);
		List<UserRole> roles = roleNames.stream()
			.map(name -> UserRole.valueOf((String) name)).toList();

		AuthUser authUser = new AuthUser(userId, username, roles);
		JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(authUser);
		SecurityContextHolder.getContext().setAuthentication(authenticationToken);
	}

	private void sendError(HttpServletResponse response, ErrorCode errorCode) throws IOException {
		response.setStatus(errorCode.getStatus().value());
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		ErrorResponse errorResponse = new ErrorResponse(errorCode);

		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(errorResponse);
		response.getWriter().write(json);
	}
}
