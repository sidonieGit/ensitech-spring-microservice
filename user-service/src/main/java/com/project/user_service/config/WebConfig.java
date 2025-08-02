package com.project.user_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // On autorise les requêtes de l'application Angular
                registry.addMapping("/api/**") // S'applique à tous les endpoints sous /api/
                        .allowedOrigins("http://localhost:4200") // L'origine du client Angular
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Méthodes HTTP autorisées
                        .allowedHeaders("*") // Tous les en-têtes sont autorisés
                        .allowCredentials(true); // Autorise l'envoi de cookies/credentials si besoin
            }
        };
    }
}
