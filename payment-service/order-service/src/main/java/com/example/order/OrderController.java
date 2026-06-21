package com.example.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping("/place")
    public Map<String, Object> placeOrder(@RequestBody(required = false) Map<String, Object> body) {
        String orderId = "ord_" + UUID.randomUUID().toString().substring(0, 12);
        double amount = body != null && body.get("amount") != null
                ? ((Number) body.get("amount")).doubleValue()
                : Math.round(ThreadLocalRandom.current().nextDouble(10, 500) * 100) / 100.0;

        log.info("Placing order orderId={} amount={}", orderId, amount);

        try {
            // Eureka-discovered call. OTel auto-instruments RestTemplate and
            // propagates the trace context as HTTP headers automatically.
            @SuppressWarnings("unchecked")
            Map<String, Object> paymentResp = restTemplate.postForObject(
                    "http://payment-service/api/payment/process",
                    Map.of("orderId", orderId, "amount", amount),
                    Map.class
            );

            log.info("Order placed successfully orderId={} txnId={}",
                    orderId, paymentResp != null ? paymentResp.get("transactionId") : "n/a");

            return Map.of(
                "status", "PLACED",
                "orderId", orderId,
                "amount", amount,
                "payment", paymentResp
            );
        } catch (Exception ex) {
            log.error("Order failed orderId={} reason={}", orderId, ex.getMessage());
            return Map.of(
                "status", "FAILED",
                "orderId", orderId,
                "error", ex.getMessage()
            );
        }
    }
}
