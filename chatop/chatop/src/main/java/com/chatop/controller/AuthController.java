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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentification", description = "Inscription, connexion et récupération des utilisateurs")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    @Operation(summary = "Inscription d’un utilisateur", description = "Crée un compte utilisateur à partir des données fournies.")
    public ResponseEntity<?> register(@RequestBody User user) {
        User savedUser = userService.saveUser(user);
        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/login")
    @Operation(summary = "Connexion d’un utilisateur", description = "Vérifie les identifiants et renvoie un token JWT.")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        User user = userRepository.findByEmail(loginDTO.getEmail());

        if (user == null || !user.getPassword().equals(loginDTO.getPassword())) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user.getEmail());
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

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
