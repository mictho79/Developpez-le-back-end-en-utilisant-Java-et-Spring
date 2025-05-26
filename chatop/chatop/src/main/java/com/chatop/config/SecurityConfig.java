package com.chatop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


import java.util.List;

@Configuration
public class SecurityConfig {

  private final JwtAuthFilter jwtAuthFilter;

  public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
    this.jwtAuthFilter = jwtAuthFilter;
  }

  /**
   * Déclare le filtre de sécurité global de l'application
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
      .csrf(csrf -> csrf.disable()) // Désactive CSRF car l'API est utilisée via Angular (pas un formulaire HTML natif)
      .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Applique la config CORS
      .authorizeHttpRequests(auth -> auth
        .requestMatchers(
          "/api/auth/login",
          "/api/auth/register",
          "/uploads/**",            // Laisse l’accès aux images
          "/swagger-ui/**",         // Swagger
          "/swagger-ui.html",
          "/v3/api-docs/**"
        ).permitAll()                    // Ces routes sont publiques
        .anyRequest().authenticated()    // Le reste nécessite un JWT valide
      )
      .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class) // Ajoute le filtre JWT
      .build();
  }

  /**
   * Déclare une configuration CORS (origines, méthodes, headers autorisés)
   */
  @Bean
  public UrlBasedCorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowedOrigins(List.of("http://localhost:4200"));
    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    config.setAllowedHeaders(List.of("*"));
    config.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return source;
  }

  /**
   * Déclare l'encodeur de mot de passe BCrypt pour sécuriser les mots de passe en base
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(); // BCrypt est robuste et recommandé pour stocker des mots de passe
  }
}
