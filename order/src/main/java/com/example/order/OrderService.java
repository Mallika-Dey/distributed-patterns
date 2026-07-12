package com.example.order;

import com.example.order.client.WorkflowClient;
import com.example.order.dto.CreateOrderRequest;
import com.example.order.dto.StartWorkflowRequest;
import com.example.order.entity.Order;
import com.example.order.entity.OrderStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
//@Transactional
public class OrderService {
    private final OrderRepository repository;
    private final WorkflowClient workflowClient;
    private final OrderPersistenceService orderPersistenceService;

    public Order create(CreateOrderRequest request) {
        Order order = orderPersistenceService.save(request); // remove if outbox used

        workflowClient.start(
                StartWorkflowRequest.builder()
                        .orderId(order.getId())
                        .amount(order.getAmount())
                        .couponCode(order.getCouponCode())
                        .build()
        );
        return order;
    }

    @Transactional
    public void complete(Long id) {
        System.out.println("Updating order : " + id);

        System.out.println(repository.findAll());
        Order order = repository.findById(id)
                .orElseThrow();

        order.setStatus(OrderStatus.COMPLETED);

    }
}