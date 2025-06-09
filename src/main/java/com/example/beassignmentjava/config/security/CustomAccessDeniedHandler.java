package com.example.beassignmentjava.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
		AccessDeniedException accessDeniedException) throws IOException {

		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		Map<String, Object> body = new HashMap<>();
		Map<String, Object> error = new HashMap<>();
		error.put("code", "ACCESS_DENIED");
		error.put("message", "접근 권한이 없습니다.");
		body.put("error", error);

		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(body);
		response.getWriter().write(json);
	}
}
