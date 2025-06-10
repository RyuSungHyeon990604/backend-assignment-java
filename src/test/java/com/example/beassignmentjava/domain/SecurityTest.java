package com.example.beassignmentjava.domain;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.beassignmentjava.domain.auth.enums.UserRole;
import com.example.beassignmentjava.util.JwtUtil;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private JwtUtil jwtUtil;

	@Test
	@DisplayName("토큰 없이 요청 시 인증 실패")
	void requestWithoutToken_shouldReturn401() throws Exception {
		// when & then
		mockMvc.perform(patch("/admin/users/1/roles")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isUnauthorized())
			.andExpect(jsonPath("$.error.message").value("유효하지 않은 인증 토큰입니다."));
	}

	@Test
	@DisplayName("잘못된 형식의 토큰 요청 시 인증 실패")
	void requestWithMalformedToken_shouldReturn401() throws Exception {
		// when & then
		mockMvc.perform(patch("/admin/users/1/roles")
				.header("Authorization", "Bearer invalid.token.structure")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isUnauthorized())
			.andExpect(jsonPath("$.error.message").value("유효하지 않은 JWT 서명입니다."));
	}

	@Test
	@DisplayName("권한 없는 일반 유저 토큰으로 요청 시 접근 거부")
	void requestWithUserToken_shouldReturn403() throws Exception {
		// given
		String token = jwtUtil.createToken(2L, "normalUser", "normalNick", List.of(UserRole.ROLE_USER));

		// when & then
		mockMvc.perform(patch("/admin/users/1/roles")
				.header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isForbidden())
			.andExpect(jsonPath("$.error.message").value("접근 권한이 없습니다."));
	}
}
