package ar.edu.um.gestion_empleados.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI myOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl("http://localhost:8080");
        devServer.setDescription("Server URL en entorno de desarrollo local");

        Server prodServer = new Server();
        prodServer.setUrl("https://gestion-empleados-api.example.com");
        prodServer.setDescription("Server URL en entorno de producción");

        Contact contact = new Contact();
        contact.setEmail("info@gestion-empleados.com");
        contact.setName("API Soporte");
        contact.setUrl("https://www.gestion-empleados.com");

        License mitLicense = new License().name("MIT License").url("https://choosealicense.com/licenses/mit/");

        Info info = new Info()
                .title("Sistema de Gestión de Empleados API")
                .version("1.0")
                .contact(contact)
                .description("Esta API expone endpoints para gestionar empleados, departamentos y proyectos.")
                .termsOfService("https://www.gestion-empleados.com/terms")
                .license(mitLicense);

        return new OpenAPI().info(info).servers(List.of(devServer, prodServer));
    }
}