package com.example.discount.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplyDiscountCommand {
    private Long orderId;
    private Double amount;
    private String couponCode;
}
