package com.example.discount.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiscountResponse {

    private Double discount;

    private Double finalAmount;

}
