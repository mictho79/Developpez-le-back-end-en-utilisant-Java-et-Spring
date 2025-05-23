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

    public void sendMessage(String email, MessageRequestDTO dto) {
        User sender = userRepository.findByEmail(email);
        Rental rental = rentalRepository.findById(dto.getRental_id())
                .orElseThrow(() -> new RuntimeException("Rental not found"));

        Message msg = new Message();
        msg.setMessage(dto.getMessage());
        msg.setSender(sender);
        msg.setRental(rental);
        msg.setCreatedAt(LocalDateTime.now());

        messageRepository.save(msg);
    }
}