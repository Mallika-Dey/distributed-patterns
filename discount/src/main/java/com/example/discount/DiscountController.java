package com.example.discount;

import com.example.discount.dto.DiscountRequest;
import com.example.discount.dto.DiscountResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/discount")
public class DiscountController {

    private final DiscountService discountService;

    @PostMapping("/apply")
    public DiscountResponse apply(@RequestBody DiscountRequest request) {

        return discountService.apply(request);

    }

}
