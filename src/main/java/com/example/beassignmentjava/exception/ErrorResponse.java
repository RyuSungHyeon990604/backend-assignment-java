package com.example.beassignmentjava.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {
	private ErrorDetail error;

	public ErrorResponse(String code, String message) {
		this.error = new ErrorDetail(code, message);
	}

	@Getter
	@AllArgsConstructor
	private static class ErrorDetail {
		private String code;
		private String message;
	}
}
