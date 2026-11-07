package com.example.order.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateOrderRequest {

    private Long productId;

    private Integer quantity;

    private BigDecimal totalPrice;
}
