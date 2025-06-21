package com.example.pagila_api.controller;

import com.example.pagila_api.model.Inventory;
import com.example.pagila_api.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public Page<Inventory> getAllInventory(Pageable pageable) {
        return inventoryService.getAllInventory(pageable);
    }

    @GetMapping("/all")
    public List<Inventory> getAllInventoryAsList() {
        return inventoryService.getAllInventory();
    }

    @GetMapping("/{id}")
    public Inventory getInventoryById(@PathVariable Integer id) {
        return inventoryService.getInventoryById(id);
    }

    @GetMapping("/{id}/with-film")
    public Inventory getInventoryByIdWithFilm(@PathVariable Integer id) {
        return inventoryService.getInventoryByIdWithFilm(id);
    }

    @GetMapping("/{id}/with-details")
    public Inventory getInventoryByIdWithFilmAndStore(@PathVariable Integer id) {
        return inventoryService.getInventoryByIdWithFilmAndStore(id);
    }

    @GetMapping("/by-film/{filmId}")
    public List<Inventory> getInventoryByFilmId(@PathVariable Integer filmId) {
        return inventoryService.getInventoryByFilmId(filmId);
    }

    @GetMapping("/by-film/{filmId}/paginated")
    public Page<Inventory> getInventoryByFilmIdPaginated(@PathVariable Integer filmId, Pageable pageable) {
        return inventoryService.getInventoryByFilmId(filmId, pageable);
    }

    @GetMapping("/by-store/{storeId}")
    public List<Inventory> getInventoryByStoreId(@PathVariable Integer storeId) {
        return inventoryService.getInventoryByStoreId(storeId);
    }

    @GetMapping("/by-store/{storeId}/paginated")
    public Page<Inventory> getInventoryByStoreIdPaginated(@PathVariable Integer storeId, Pageable pageable) {
        return inventoryService.getInventoryByStoreId(storeId, pageable);
    }

    @GetMapping("/by-film-and-store")
    public List<Inventory> getInventoryByFilmAndStore(
            @RequestParam Integer filmId,
            @RequestParam Integer storeId) {
        return inventoryService.getInventoryByFilmAndStore(filmId, storeId);
    }

    @PostMapping
    public ResponseEntity<Inventory> createInventory(@Valid @RequestBody Inventory inventory) {
        Inventory createdInventory = inventoryService.createInventory(inventory);
        return ResponseEntity.status(201).body(createdInventory);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Inventory> updateInventory(@PathVariable Integer id, @Valid @RequestBody Inventory inventory) {
        Inventory updatedInventory = inventoryService.updateInventory(id, inventory);
        return ResponseEntity.ok(updatedInventory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInventory(@PathVariable Integer id) {
        inventoryService.deleteInventory(id);
        return ResponseEntity.noContent().build();
    }

    // Count endpoints
    @GetMapping("/count/by-film/{filmId}")
    public ResponseEntity<Long> getInventoryCountByFilmId(@PathVariable Integer filmId) {
        Long count = inventoryService.getInventoryCountByFilmId(filmId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/by-store/{storeId}")
    public ResponseEntity<Long> getInventoryCountByStoreId(@PathVariable Integer storeId) {
        Long count = inventoryService.getInventoryCountByStoreId(storeId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/by-film-and-store")
    public ResponseEntity<Long> getInventoryCountByFilmAndStore(
            @RequestParam Integer filmId,
            @RequestParam Integer storeId) {
        Long count = inventoryService.getInventoryCountByFilmAndStore(filmId, storeId);
        return ResponseEntity.ok(count);
    }

    // Availability endpoints
    @GetMapping("/available")
    public List<Inventory> getAvailableInventory() {
        return inventoryService.getAvailableInventory();
    }

    @GetMapping("/available/by-film/{filmId}")
    public List<Inventory> getAvailableInventoryByFilmId(@PathVariable Integer filmId) {
        return inventoryService.getAvailableInventoryByFilmId(filmId);
    }

    @GetMapping("/available/by-store/{storeId}")
    public List<Inventory> getAvailableInventoryByStoreId(@PathVariable Integer storeId) {
        return inventoryService.getAvailableInventoryByStoreId(storeId);
    }

    @GetMapping("/{id}/is-available")
    public ResponseEntity<Boolean> isInventoryAvailable(@PathVariable Integer id) {
        boolean isAvailable = inventoryService.isInventoryAvailable(id);
        return ResponseEntity.ok(isAvailable);
    }
}