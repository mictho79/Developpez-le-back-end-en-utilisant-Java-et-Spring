package com.chatop.controller;

import com.chatop.dto.RentalDTO;
import com.chatop.dto.RentalRequestDTO;
import com.chatop.dto.RentalUpdateDTO;
import com.chatop.service.RentalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/api/rentals")
@Tag(name = "Rentals", description = "CRUD des locations de biens immobiliers")
public class RentalController {

  @Autowired
  private RentalService rentalService;

  /**
   * Récupère toutes les locations (GET /api/rentals)
   */
  @GetMapping
  @Operation(summary = "Liste toutes les locations", description = "Retourne toutes les annonces de locations disponibles.")
  public Map<String, List<RentalDTO>> getAllRentals() {
    return Map.of("rentals", rentalService.getAllRentals());
  }

  /**
   * Crée une nouvelle location avec image (POST /api/rentals)
   * Authentifie l'utilisateur via JWT, enregistre l'image localement.
   */
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @Operation(summary = "Créer une location", description = "Ajoute une nouvelle annonce de location avec image.")
  public RentalDTO createRental(
    @RequestParam("name") String name,
    @RequestParam("surface") int surface,
    @RequestParam("price") int price,
    @RequestParam("picture") MultipartFile picture,
    @RequestParam("description") String description,
    Authentication authentication
  ) throws IOException {
    String email = (String) authentication.getPrincipal();

    // Sauvegarde de l’image dans le dossier /uploads
    String filename = picture.getOriginalFilename();
    Path path = Paths.get("uploads", filename);
    Files.createDirectories(path.getParent());
    Files.write(path, picture.getBytes());

    // Construction du DTO pour le service
    RentalRequestDTO dto = new RentalRequestDTO();
    dto.setName(name);
    dto.setSurface(surface);
    dto.setPrice(price);
    dto.setPicture("http://localhost:3001/uploads/" + filename);
    dto.setDescription(description);

    return rentalService.createRental(dto, email);
  }

  /**
   * Récupère une location par ID (GET /api/rentals/{id})
   */
  @GetMapping("/{id}")
  @Operation(summary = "Récupérer une location", description = "Récupère les détails d’une annonce à partir de son ID.")
  public RentalDTO getRentalById(@PathVariable Long id) {
    return rentalService.getRentalById(id);
  }

  /**
   * Met à jour une location (PUT /api/rentals/{id})
   */
  @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @Operation(summary = "Mettre à jour une location", description = "Modifie les données d’une annonce de location existante.")
  public ResponseEntity<Map<String, String>> updateRental(
    @PathVariable Long id,
    @RequestParam("name") String name,
    @RequestParam("surface") int surface,
    @RequestParam("price") int price,
    @RequestParam("description") String description
  ) {
    RentalUpdateDTO dto = new RentalUpdateDTO();
    dto.setName(name);
    dto.setSurface(surface);
    dto.setPrice(price);
    dto.setDescription(description);

    rentalService.updateRental(id, dto);
    return ResponseEntity.ok(Map.of("message", "Rental updated !"));
  }
}
