package com.example.workflow.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StartWorkflowRequest {

    private Long orderId;

    private Double amount;

    private String couponCode;

}
