package br.com.elysian.tartarus.config.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Profile("development")
@Configuration
@EnableWebMvc
public class DevelopmentWebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Allow Cors to localhost (Web application React)
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:3000/")
                .allowCredentials(true).maxAge(3600);

    }

}