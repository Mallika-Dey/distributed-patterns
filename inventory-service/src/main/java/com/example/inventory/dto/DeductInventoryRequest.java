package com.example.inventory.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeductInventoryRequest {

    private Long productId;
    private Integer quantity;
}
