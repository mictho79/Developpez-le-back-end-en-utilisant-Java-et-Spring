package com.chatop.service;

import com.chatop.dto.RentalDTO;
import com.chatop.dto.RentalRequestDTO;
import com.chatop.dto.RentalUpdateDTO;
import com.chatop.model.Rental;
import com.chatop.model.User;
import com.chatop.repository.RentalRepository;
import com.chatop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RentalService {

  @Autowired
  private RentalRepository rentalRepository;

  @Autowired
  private UserRepository userRepository;

  /**
   * Retourne toutes les locations disponibles, converties en DTO.
   */
  public List<RentalDTO> getAllRentals() {
    return rentalRepository.findAll().stream()
      .map(r -> new RentalDTO(
        r.getId(),
        r.getName(),
        r.getSurface(),
        r.getPrice(),
        r.getPicture(),
        r.getDescription(),
        r.getOwner_id(),
        r.getCreated_at(),
        r.getUpdated_at()
      ))
      .toList();
  }

  /**
   * Crée une nouvelle annonce de location à partir d’un DTO et de l'email du propriétaire.
   */
  public RentalDTO createRental(RentalRequestDTO dto, String ownerEmail) {
    User owner = userRepository.findByEmail(ownerEmail);

    Rental rental = new Rental();
    rental.setName(dto.getName());
    rental.setSurface(dto.getSurface());
    rental.setPrice(dto.getPrice());
    rental.setPicture(dto.getPicture());
    rental.setDescription(dto.getDescription());
    rental.setOwner_id(owner.getId());
    rental.setCreated_at(LocalDateTime.now());
    rental.setUpdated_at(LocalDateTime.now());

    Rental saved = rentalRepository.save(rental);

    return new RentalDTO(
      saved.getId(),
      saved.getName(),
      saved.getSurface(),
      saved.getPrice(),
      saved.getPicture(),
      saved.getDescription(),
      saved.getOwner_id(),
      saved.getCreated_at(),
      saved.getUpdated_at()
    );
  }

  /**
   * Récupère une annonce de location par son ID.
   */
  public RentalDTO getRentalById(Long id) {
    Rental rental = rentalRepository.findById(id)
      .orElseThrow(() -> new RuntimeException("Rental not found"));

    return new RentalDTO(
      rental.getId(),
      rental.getName(),
      rental.getSurface(),
      rental.getPrice(),
      rental.getPicture(),
      rental.getDescription(),
      rental.getOwner_id(),
      rental.getCreated_at(),
      rental.getUpdated_at()
    );
  }

  /**
   * Met à jour une annonce existante.
   * Le champ image n’est pas modifiable ici.
   */
  public void updateRental(Long id, RentalUpdateDTO dto) {
    Rental rental = rentalRepository.findById(id)
      .orElseThrow(() -> new RuntimeException("Rental not found"));

    rental.setName(dto.getName());
    rental.setSurface(dto.getSurface());
    rental.setPrice(dto.getPrice());
    rental.setDescription(dto.getDescription());
    rental.setUpdated_at(LocalDateTime.now());

    rentalRepository.save(rental);
  }
}
