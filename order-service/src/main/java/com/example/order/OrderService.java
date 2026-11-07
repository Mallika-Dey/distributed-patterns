package com.example.order;
import com.example.order.client.DeductInventoryRequest;
import com.example.order.client.InventoryClient;
import com.example.order.dto.CreateOrderRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;

    @Transactional
    public Order createOrder(CreateOrderRequest request) {
        // Deduct inventory from Inventory Service
        inventoryClient.deduct(
                new DeductInventoryRequest(
                        request.getProductId(),
                        request.getQuantity()
                )
        );

        Order order = new Order();

        order.setProductId(request.getProductId());
        order.setQuantity(request.getQuantity());
        order.setTotalPrice(request.getTotalPrice());
        order.setStatus("CREATED");
        order.setCreatedAt(LocalDateTime.now());

        return orderRepository.save(order);
    }
}