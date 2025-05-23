package com.chatop.dto;

import lombok.Data;

@Data
public class RentalRequestDTO {
    private String name;
    private int surface;
    private int price;
    private String picture;
    private String description;
}
