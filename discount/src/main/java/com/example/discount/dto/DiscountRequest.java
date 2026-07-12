package com.example.discount.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiscountRequest {
    private Long orderId;
    private Double amount;
    private String couponCode;
}
