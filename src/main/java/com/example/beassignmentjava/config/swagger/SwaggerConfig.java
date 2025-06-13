package com.example.beassignmentjava.config.swagger;


import com.example.beassignmentjava.exception.ErrorCode;
import com.example.beassignmentjava.exception.ErrorResponse;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
	@Bean
	public OpenAPI customOpenApi() {
		return new OpenAPI()
			.info(new Info()
				.title("BackEnd-Assignment-Java API")
				.description("바로인턴 직무과제 (JAVA)")
			).components(jwtComponents())
			.addServersItem(new Server().url("/"))
			.addSecurityItem(new SecurityRequirement().addList("jwt"));
	}

	private Components jwtComponents() {
		SecurityScheme securityScheme = new SecurityScheme()
			.type(SecurityScheme.Type.HTTP)
			.in(SecurityScheme.In.HEADER)
			.name("Authorization")
			.scheme("Bearer")
			.bearerFormat("JWT");
		return new Components()
			.addSecuritySchemes("jwt", securityScheme);
	}

	@Bean
	public OperationCustomizer customizer() {
		return (operation, handlerMethod) -> {
			ApiErrorResponse apiErrorResponse = handlerMethod.getMethodAnnotation(ApiErrorResponse.class);
			if(apiErrorResponse != null) {
				generateSwaggerErrorResponse(operation, apiErrorResponse.error(), apiErrorResponse.message());
			}

			ApiErrorResponses apiErrorResponses = handlerMethod.getMethodAnnotation(ApiErrorResponses.class);
			if(apiErrorResponses != null) {
				for (ApiErrorResponse errorResponse : apiErrorResponses.value()) {
					generateSwaggerErrorResponse(operation, errorResponse.error(), errorResponse.message());
				}
			}

			return operation;
		};
	}

	private void generateSwaggerErrorResponse(Operation op, ErrorCode errorCode, String message) {
		ApiResponses apiResponses = op.getResponses();
		apiResponses.addApiResponse(errorCode.getStatus().toString(), createJsonErrorResponse(errorCode, message));
	}

	private ApiResponse createJsonErrorResponse(ErrorCode errorCode, String message) {
		ApiResponse apiResponse = new ApiResponse();
		Content content = new Content();
		MediaType type = new MediaType();

		Example example = new Example();
		example.description(message);
		example.setValue(new ErrorResponse(errorCode));

		type.addExamples(errorCode.name(), example);
		content.addMediaType("application/json", type);

		apiResponse.setContent(content);
		return apiResponse;
	}
}
