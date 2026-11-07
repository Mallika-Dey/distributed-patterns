package com.example.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "inventory-service",
        url = "${inventory.service.url}"
)
public interface InventoryClient {

    @PostMapping("/api/inventory/deduct")
    String deduct(@RequestBody DeductInventoryRequest request);
}
