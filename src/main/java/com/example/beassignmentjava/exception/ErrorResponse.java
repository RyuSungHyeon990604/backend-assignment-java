package com.example.beassignmentjava.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "에러 응답 객체")
public class ErrorResponse {
	@Schema(description = "에러 상세 정보", implementation = ErrorDetail.class)
	private ErrorDetail error;

	public ErrorResponse(String code, String message) {
		this.error = new ErrorDetail(code, message);
	}

	@Getter
	@AllArgsConstructor
	@Schema(description = "에러 상세 필드")
	private static class ErrorDetail {
		@Schema(description = "에러 코드", example = "INVALID_USER")
		private String code;

		@Schema(description = "에러 메시지", example = "유효하지 않은 유저입니다.")
		private String message;
	}
}
