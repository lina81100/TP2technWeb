package comptoirs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class WebApp {

	public static void main(String[] args) {
		SpringApplication.run(WebApp.class, args);
	}
	
	/**
	 * Configure CORS for the application.
	 * Permet l'accès à l'application depuis un autre domaine (Cross-Origin).
	 * @return la configuation de CORS
	 */
	@Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*"); // for /** means all mapping URL, and * for all domain
            }
        };
    }	

	

}
