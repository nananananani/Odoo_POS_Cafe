package com.cafepos.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI cafePosOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Cafe POS API")
                        .description("Cafe POS Application API Documentation")
                        .version("v1.0"));
    }
}
