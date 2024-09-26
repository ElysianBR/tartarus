package br.com.elysian.tartarus.config.documentation;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Elysian docs",
                description = "Elysian documentation API",
                version = "1.0.0",
                contact = @Contact(name = "Elysian", url = "https://elysian.com.br", email = "development@elysian.com.br")
        )
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class SwaggerConfig {

    private final CustomOpenApiCustomizer customOpenApiCustomizer;

    public SwaggerConfig(CustomOpenApiCustomizer customOpenApiCustomizer) {
        this.customOpenApiCustomizer = customOpenApiCustomizer;
    }

    @Bean
    public GroupedOpenApi v1Api() {
        return GroupedOpenApi.builder()
                .group("v1")
                .pathsToMatch("/api/v1/**")
                .displayName("version 1")
                .addOperationCustomizer(new CustomOperationCustomizer())
                .addOpenApiCustomizer(customOpenApiCustomizer)
                .build();
    }

}
