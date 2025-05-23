package com.chatop.dto;

import lombok.Data;

@Data
public class RentalUpdateDTO {
    private String name;
    private int surface;
    private int price;
    private String description;
}