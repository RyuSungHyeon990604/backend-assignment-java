package com.example.beassignmentjava.config.swagger;

import com.example.beassignmentjava.exception.ErrorCode;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(ApiErrorResponses.class)
public @interface ApiErrorResponse {
	ErrorCode error() default ErrorCode.INVALID_USER;
	String message() default "";
}
