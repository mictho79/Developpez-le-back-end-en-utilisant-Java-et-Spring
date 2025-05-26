package com.chatop.service;

import com.chatop.dto.MessageRequestDTO;
import com.chatop.model.Message;
import com.chatop.model.Rental;
import com.chatop.model.User;
import com.chatop.repository.MessageRepository;
import com.chatop.repository.RentalRepository;
import com.chatop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
@Service
public class MessageService {

  @Autowired
  private MessageRepository messageRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RentalRepository rentalRepository;

  /**
   * Envoie un message en liant un utilisateur et une annonce de location.
   * Les données sont extraites du DTO et de l’email JWT.
   */
  public void sendMessage(String email, MessageRequestDTO dto) {
    // Récupère l’expéditeur à partir de l’email (injecté via JWT)
    User sender = userRepository.findByEmail(email);

    // Vérifie que la location existe
    Rental rental = rentalRepository.findById(dto.getRental_id())
      .orElseThrow(() -> new RuntimeException("Rental not found"));

    // Création du message à partir du DTO
    Message msg = new Message();
    msg.setMessage(dto.getMessage());
    msg.setSender(sender);
    msg.setRental(rental);
    msg.setCreatedAt(LocalDateTime.now());

    // Enregistrement en base
    messageRepository.save(msg);
  }
}
