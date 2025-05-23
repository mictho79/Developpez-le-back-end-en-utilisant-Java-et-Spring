package com.chatop.dto;

import lombok.Data;

@Data
public class MessageRequestDTO {
    private String message;
    private Long rental_id;
}