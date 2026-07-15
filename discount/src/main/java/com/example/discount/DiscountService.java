package com.example.discount;

import com.example.discount.dto.ApplyDiscountCommand;
import com.example.discount.dto.DiscountResponse;
import org.springframework.stereotype.Service;

@Service
public class DiscountService {
    public DiscountResponse apply(ApplyDiscountCommand request) {
        double discount = request.getAmount() * 0.10;

        return DiscountResponse.builder()
                .discount(discount)
                .finalAmount(request.getAmount() - discount)
                .build();
    }

}
