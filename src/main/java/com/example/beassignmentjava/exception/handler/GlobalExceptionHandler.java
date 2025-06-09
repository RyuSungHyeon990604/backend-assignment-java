package com.example.beassignmentjava.exception.handler;

import com.example.beassignmentjava.exception.ApplicationException;
import com.example.beassignmentjava.exception.ErrorCode;
import com.example.beassignmentjava.exception.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(ApplicationException.class)
	public ResponseEntity<ErrorResponse> responseStatusExceptionException(ApplicationException e) {
		ErrorCode errorCode = e.getError();
		return ResponseEntity.status(errorCode.getStatus()).body(new ErrorResponse(errorCode.name(), errorCode.getMessage()));
	}
}
