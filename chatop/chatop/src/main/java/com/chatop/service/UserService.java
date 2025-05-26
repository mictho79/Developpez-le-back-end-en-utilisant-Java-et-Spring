package com.chatop.service;

import com.chatop.model.User;
import com.chatop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service // Déclare un composant de service Spring, injectable dans d'autres classes
public class UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder; // Utilisé pour sécuriser les mots de passe avec BCrypt

  /**
   * Sauvegarde un nouvel utilisateur en encodant son mot de passe.
   * Appelée à l'inscription.
   */
  public User saveUser(User user) {
    user.setPassword(passwordEncoder.encode(user.getPassword())); // 🔐 Hachage du mot de passe
    return userRepository.save(user);
  }

  /**
   * Recherche un utilisateur par email.
   * Utilisé lors de la connexion ou depuis le token JWT.
   */
  public Optional<User> getUserByEmail(String email) {
    return Optional.ofNullable(userRepository.findByEmail(email));
  }

  /**
   * Recherche un utilisateur par ID.
   * Utilisé pour retourner les infos d'un utilisateur depuis son identifiant.
   */
  public Optional<User> getUserById(Long id) {
    return userRepository.findById(id);
  }
}
