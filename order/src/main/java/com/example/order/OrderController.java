package com.example.order;

import com.example.order.dto.CreateOrderRequest;
import com.example.order.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final OrderService service;

    @PostMapping
    public Order create(@RequestBody CreateOrderRequest request) {
        return service.create(request);
    }
}
