package com.example.order.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StartWorkflowRequest {
    private Long orderId;
    private Double amount;
    private String couponCode;
}
