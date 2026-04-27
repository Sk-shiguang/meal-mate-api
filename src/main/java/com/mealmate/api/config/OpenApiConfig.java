package com.mealmate.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI mealMateOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("MealMate 后端接口")
                        .description("MealMate / 吃什么小程序 Java 后端服务接口文档")
                        .version("0.1.0"));
    }
}
