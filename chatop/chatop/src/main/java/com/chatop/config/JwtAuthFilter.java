package com.chatop.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

  private final JwtUtil jwtUtil;

  // Injection de la classe utilitaire qui gère les JWT
  public JwtAuthFilter(JwtUtil jwtUtil) {
    this.jwtUtil = jwtUtil;
  }

  /**
   * Filtrage de chaque requête HTTP entrante pour vérifier le token JWT.
   */
  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain)
    throws ServletException, IOException {

    // Récupère le header Authorization
    String authHeader = request.getHeader("Authorization");
    System.out.println("🛂 Authorization Header: " + authHeader);

    // Si le token commence par "Bearer ", on le traite
    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      String token = authHeader.substring(7); // Enlève "Bearer "
      try {
        String email = jwtUtil.extractEmail(token); // Décode le JWT
        System.out.println("✅ Email extrait : " + email);

        // Injecte les infos dans le contexte de sécurité Spring
        UsernamePasswordAuthenticationToken auth =
          new UsernamePasswordAuthenticationToken(email, null, Collections.emptyList());

        SecurityContextHolder.getContext().setAuthentication(auth);

      } catch (Exception e) {
        System.out.println("❌ Erreur JWT : " + e.getMessage());
      }
    }

    // Passe au filtre suivant
    filterChain.doFilter(request, response);
  }
}
