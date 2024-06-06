package romatattoo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Permitir solicitudes en todas las rutas
                .allowedOrigins("http://localhost:4200") // Permitir solicitudes desde este origen
                .allowedMethods("GET", "POST", "PUT", "DELETE"); // Permitir estos métodos HTTP
    }
}