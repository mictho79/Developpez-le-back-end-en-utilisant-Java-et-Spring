package com.chatop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // Indique que cette classe contient une configuration Spring
public class CorsConfig {

  @Bean
  public WebMvcConfigurer corsConfigurer() {
    // Permet de configurer les règles CORS de manière globale
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        // Applique les règles aux routes commençant par /api/**
        registry.addMapping("/api/**")
          .allowedOrigins("http://localhost:4200") // Autorise les requêtes du front local Angular
          .allowedMethods("*") // Autorise toutes les méthodes HTTP (GET, POST, etc.)
          .allowedHeaders("*") // Autorise tous les headers
          .allowCredentials(true); // Permet l'envoi de cookies/tokens d’authentification
      }
    };
  }
}
