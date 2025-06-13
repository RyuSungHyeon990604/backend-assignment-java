package com.example.beassignmentjava.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
	USER_ALREADY_EXISTS("이미 가입된 사용자입니다.", BAD_REQUEST),
	ALREADY_ADMIN("이미 관리자입니다.", BAD_REQUEST),
	INVALID_CREDENTIALS("아이디 또는 비밀번호가 올바르지 않습니다.", UNAUTHORIZED),
	ID_ALREADY_ASSIGNED("이미 ID가 할당된 사용자입니다.", BAD_REQUEST),
	INVALID_TOKEN("유효하지 않은 인증 토큰입니다.", UNAUTHORIZED),
	INVALID_USER("유효하지 않은 유저입니다.", NOT_FOUND),
	ACCESS_DENIED("접근 권한이 없습니다.",FORBIDDEN),
	INVALID_ROLE("유효하지 않은 권한입니다.", UNAUTHORIZED);


	private final String message;
	private final HttpStatus status;
}
