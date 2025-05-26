package com.chatop.controller;

import com.chatop.config.JwtUtil;
import com.chatop.dto.LoginDTO;
import com.chatop.dto.LoginResponseDTO;
import com.chatop.dto.UserDTO;
import com.chatop.model.User;
import com.chatop.repository.UserRepository;
import com.chatop.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController // Déclare cette classe comme contrôleur REST, les méthodes renvoient des réponses HTTP JSON
@RequestMapping("/api/auth") // Préfixe d’URL pour toutes les routes de ce contrôleur
@Tag(name = "Authentification", description = "Inscription, connexion et récupération des utilisateurs")
public class AuthController {

  @Autowired
  private UserRepository userRepository; // Accès direct à la base de données utilisateurs

  @Autowired
  private UserService userService; // Service contenant la logique métier utilisateur

  @Autowired
  private JwtUtil jwtUtil; // Classe utilitaire pour générer les tokens JWT

  @Autowired
  private PasswordEncoder passwordEncoder; // Encodeur pour hacher les mots de passe

  /**
   * Inscription d’un utilisateur (POST /api/auth/register)
   * Reçoit un objet `User` et le sauvegarde via le service.
   */
  @PostMapping("/register")
  @Operation(summary = "Inscription d’un utilisateur", description = "Crée un compte utilisateur à partir des données fournies.")
  public ResponseEntity<?> register(@RequestBody User user) {
    User savedUser = userService.saveUser(user);
    String token = jwtUtil.generateToken(savedUser.getEmail()); // 🔐 Génère le token
    return ResponseEntity.ok(new LoginResponseDTO(token)); // 🔁 Renvoie le même format que /login
  }

  /**
   * Connexion de l'utilisateur (POST /api/auth/login)
   * Vérifie email + mot de passe, et génère un token JWT.
   */
  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
    User user = userRepository.findByEmail(loginDTO.getEmail());

    if (user == null || !passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
      return ResponseEntity.status(401).body("Invalid credentials");
    }

    String token = jwtUtil.generateToken(user.getEmail());
    return ResponseEntity.ok(new LoginResponseDTO(token));
  }

  /**
   * Récupère l'utilisateur connecté (GET /api/auth/me)
   * Extrait l'email depuis le JWT, renvoie l’objet utilisateur correspondant.
   */
  @GetMapping("/me")
  @Operation(summary = "Récupérer l’utilisateur connecté", description = "Retourne les informations du profil actuellement connecté.")
  public ResponseEntity<UserDTO> getCurrentUser(Authentication authentication) {
    String email = (String) authentication.getPrincipal();
    User user = userRepository.findByEmail(email);

    UserDTO userDTO = new UserDTO(
      user.getId(),
      user.getEmail(),
      user.getName(),
      user.getCreated_at(),
      user.getUpdated_at()
    );

    return ResponseEntity.ok(userDTO);
  }

  /**
   * Récupère un utilisateur via son ID (GET /api/auth/user/{id})
   */
  @GetMapping("/user/{id}")
  @Operation(summary = "Récupérer un utilisateur par ID", description = "Retourne les informations d’un utilisateur spécifique.")
  public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
    return userRepository.findById(id)
      .map(user -> new UserDTO(
        user.getId(),
        user.getEmail(),
        user.getName(),
        user.getCreated_at(),
        user.getUpdated_at()
      ))
      .map(ResponseEntity::ok)
      .orElse(ResponseEntity.notFound().build());
  }
}
