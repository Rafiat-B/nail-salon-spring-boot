package com.example.nailsalon.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(info = @Info(title = "Nail Salon API", version = "1.0", description = "API documentation for the Nail Salon application"), servers = {@Server(description = "Local ENV", url = "http://localhost:9090")})
//@EnableOpenApi
@Configuration
@ComponentScan(basePackages = "com.example.nailsalon") // Ensure it scans your package with @Controller
public class OpenApiConfig {
}
