package com.example.payment;

import com.example.payment.dto.PaymentRequest;
import com.example.payment.dto.PaymentResponse;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    public PaymentResponse pay(PaymentRequest request) {
        return PaymentResponse.builder()
                .status("SUCCESS")
                .build();
    }
}
