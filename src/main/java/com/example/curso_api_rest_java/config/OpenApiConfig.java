package com.example.curso_api_rest_java.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI customOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("RESTful API with Java 21 and Spring Boot 3")
                .version("v1")
                .description("Some description")
                .termsOfService("www.google.com")
                .license(new License().name("Apache 2.0").url("www.google.com")));
    }
}
