package cl.bci.users.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI usersApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Users API")
                        .description("API para creación y gestión de usuarios")
                        .version("v1"));
    }
}
