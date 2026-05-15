package pet.be.pet_clinic.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI petClinicOpenApi() {
		return new OpenAPI()
				.addServersItem(new Server()
						.url("http://localhost:8080")
						.description("Development Server"))
				.info(new Info()
						.title("Spring PetClinic REST API")
						.version("3.2.0")
						.description("REST API for the Spring PetClinic application. "
								+ "This API provides endpoints for managing pet owners, pets, veterinarians, and visits.")
						.contact(new Contact().name("PetClinic Team").url("https://spring-petclinic.github.io/"))
						.license(new License().name("Apache 2.0")));
	}

}
