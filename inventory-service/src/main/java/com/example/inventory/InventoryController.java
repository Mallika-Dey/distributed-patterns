package com.example.inventory;

import com.example.inventory.dto.DeductInventoryRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryRepository inventoryRepository;
    private final InventoryService inventoryService;

    @GetMapping("/{productId}")
    public ResponseEntity<Inventory> getInventory(@PathVariable Long productId) {
        return inventoryRepository.findByProductId(productId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/deduct")
    public ResponseEntity<String> deduct(
            @RequestBody DeductInventoryRequest request) {

        inventoryService.deduct(
                request.getProductId(),
                request.getQuantity()
        );

        return ResponseEntity.ok("Inventory deducted successfully");
    }
}