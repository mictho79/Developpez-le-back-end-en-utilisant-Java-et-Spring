package com.chatop.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

  private final String secret = "123456789"; // Clé secrète pour signer/valider les tokens

  /**
   * Extrait l'email (sujet) à partir d'un token JWT
   */
  public String extractEmail(String token) {
    return JWT.require(Algorithm.HMAC512(secret)) // Spécifie l’algorithme utilisé
      .build()
      .verify(token) // Vérifie la validité du token (signature)
      .getSubject(); // Retourne l’objet (ici, l’email)
  }

  /**
   * Génère un token JWT pour un email
   */
  public String generateToken(String email) {
    return JWT.create()
      .withSubject(email) // Le sujet est l'email de l'utilisateur
      .sign(Algorithm.HMAC512(secret)); // Signe avec la même clé secrète
  }
}
