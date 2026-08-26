package com.alergb.taskmanager.security.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenApi(){
        return new OpenAPI()
                .info(new Info()
                        .title("Task Manager")
                        .version("1.0")
                        .description("API RESTful for task managing")
                        .contact(new Contact()
                                .name("Alejandro Rojas")
                                .email("")
                                .url("placeholder.url")
                        )
                        .license( new License()
                                .name("Apache 2.0")
                                .url("http://www.apache.org/licesnses/LICENSE-2.0.html")
                        )
                );
    }

}
