package com.example.beassignmentjava.config.swagger;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
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
}
