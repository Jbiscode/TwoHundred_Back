package org.duckdns.bitwatchu.global.config.swagger;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;

@OpenAPIDefinition(
        info = @Info(
                title = "bitwatchu API",
                description = "bitwatchu API 문서&명세서 입니다.",
                version = "1.0.0"
        )
)
@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {

        // 문서화할 API를 그룹화하여 관리(여러개의 그룹을 만들어서 관리 가능)
        @Bean
        public GroupedOpenApi customOpenAPI(){
            String[] paths = {"/users/**","/admin/**"};
            return GroupedOpenApi.builder()
                    .group("api-v1")
                    .pathsToMatch(paths)
                    .build();
        }

        @Bean
        public OpenAPI openAPI() {
            return new OpenAPI()
                .components(new Components()
                                .addSecuritySchemes("bearer-key",
                                        new SecurityScheme()
                                            .type(SecurityScheme.Type.HTTP)
                                            .scheme("bearer")
                                            .bearerFormat("JWT")
                                            .in(SecurityScheme.In.HEADER)
                                            .name("Authorization")))
                                .addSecurityItem(new SecurityRequirement().addList("bearer-key"));
        }

}