package com.example.beassignmentjava.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {
	@Schema(description = "에러 상세 정보", implementation = ErrorDetail.class)
	private ErrorDetail error;

	public ErrorResponse(ErrorCode errorCode) {
		this.error = new ErrorDetail(errorCode.name(), errorCode.getMessage());
	}
}
