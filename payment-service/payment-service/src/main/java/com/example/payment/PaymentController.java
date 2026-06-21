package com.example.payment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);

    @PostMapping("/process")
    public Map<String, Object> process(@RequestBody Map<String, Object> req) throws InterruptedException {
        String orderId = (String) req.getOrDefault("orderId", "unknown");
        Number amount  = (Number) req.getOrDefault("amount", 0);

        log.info("Processing payment for orderId={} amount={}", orderId, amount);

        // Simulate realistic payment-gateway latency (10–80 ms)
        Thread.sleep(ThreadLocalRandom.current().nextInt(10, 80));

        // ~3% failure rate so dashboards have something to show
        if (ThreadLocalRandom.current().nextInt(100) < 3) {
            log.error("Payment declined for orderId={} amount={}", orderId, amount);
            throw new RuntimeException("Payment declined by gateway");
        }

        String txnId = "txn_" + UUID.randomUUID().toString().substring(0, 12);
        log.info("Payment successful orderId={} txnId={}", orderId, txnId);

        return Map.of(
            "status", "SUCCESS",
            "transactionId", txnId,
            "orderId", orderId,
            "amount", amount
        );
    }
}
