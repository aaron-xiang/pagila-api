package com.example.pagila_api.controller;

import com.example.pagila_api.model.Store;
import com.example.pagila_api.service.StoreService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stores")
public class StoreController {

    private final StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @GetMapping
    public Page<Store> getAllStores(Pageable pageable) {
        return storeService.getAllStores(pageable);
    }

    @GetMapping("/all")
    public List<Store> getAllStoresAsList() {
        return storeService.getAllStores();
    }

    @GetMapping("/with-details")
    public List<Store> getAllStoresWithDetails() {
        return storeService.getAllStoresWithDetails();
    }

    @GetMapping("/{id}")
    public Store getStoreById(@PathVariable Integer id) {
        return storeService.getStoreById(id);
    }

    @GetMapping("/{id}/with-address")
    public Store getStoreByIdWithAddress(@PathVariable Integer id) {
        return storeService.getStoreByIdWithAddress(id);
    }

    @GetMapping("/{id}/with-manager")
    public Store getStoreByIdWithManager(@PathVariable Integer id) {
        return storeService.getStoreByIdWithManager(id);
    }

    @GetMapping("/{id}/with-full-details")
    public Store getStoreByIdWithAddressAndManager(@PathVariable Integer id) {
        return storeService.getStoreByIdWithAddressAndManager(id);
    }

    @GetMapping("/by-manager/{managerStaffId}")
    public Store getStoreByManagerId(@PathVariable Integer managerStaffId) {
        return storeService.getStoreByManagerId(managerStaffId);
    }

    @GetMapping("/by-address/{addressId}")
    public List<Store> getStoresByAddressId(@PathVariable Integer addressId) {
        return storeService.getStoresByAddressId(addressId);
    }

    @PostMapping
    public ResponseEntity<Store> createStore(@Valid @RequestBody Store store) {
        Store createdStore = storeService.createStore(store);
        return ResponseEntity.status(201).body(createdStore);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Store> updateStore(@PathVariable Integer id, @Valid @RequestBody Store store) {
        Store updatedStore = storeService.updateStore(id, store);
        return ResponseEntity.ok(updatedStore);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStore(@PathVariable Integer id) {
        storeService.deleteStore(id);
        return ResponseEntity.noContent().build();
    }

    // Analytics endpoints
    @GetMapping("/{id}/inventory-count")
    public ResponseEntity<Long> getInventoryCount(@PathVariable Integer id) {
        Long count = storeService.getInventoryCountByStoreId(id);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/{id}/customer-count")
    public ResponseEntity<Long> getCustomerCount(@PathVariable Integer id) {
        Long count = storeService.getCustomerCountByStoreId(id);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/{id}/staff-count")
    public ResponseEntity<Long> getStaffCount(@PathVariable Integer id) {
        Long count = storeService.getStaffCountByStoreId(id);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/{id}/stats")
    public ResponseEntity<StoreService.StoreStats> getStoreStats(@PathVariable Integer id) {
        StoreService.StoreStats stats = storeService.getStoreStats(id);
        return ResponseEntity.ok(stats);
    }

    // Geographic endpoints
    @GetMapping("/by-city/{cityName}")
    public List<Store> getStoresByCity(@PathVariable String cityName) {
        return storeService.getStoresByCity(cityName);
    }

    @GetMapping("/by-country/{countryName}")
    public List<Store> getStoresByCountry(@PathVariable String countryName) {
        return storeService.getStoresByCountry(countryName);
    }

    // Performance endpoints
    @GetMapping("/high-performance")
    public List<Store> getStoresWithMoreThanXRentals(@RequestParam(defaultValue = "100") Long rentalCount) {
        return storeService.getStoresWithMoreThanXRentals(rentalCount);
    }
}