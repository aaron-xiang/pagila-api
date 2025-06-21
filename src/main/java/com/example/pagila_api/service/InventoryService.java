package com.example.pagila_api.service;

import com.example.pagila_api.exception.ResourceNotFoundException;
import com.example.pagila_api.model.Inventory;
import com.example.pagila_api.repository.InventoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public Page<Inventory> getAllInventory(Pageable pageable) {
        return inventoryRepository.findAll(pageable);
    }

    public List<Inventory> getAllInventory() {
        return inventoryRepository.findAll();
    }

    public Inventory getInventoryById(Integer id) {
        return inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found with ID: " + id));
    }

    public Inventory getInventoryByIdWithFilm(Integer id) {
        return inventoryRepository.findByIdWithFilm(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found with ID: " + id));
    }

    public Inventory getInventoryByIdWithFilmAndStore(Integer id) {
        return inventoryRepository.findByIdWithFilmAndStore(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found with ID: " + id));
    }

    public List<Inventory> getInventoryByFilmId(Integer filmId) {
        return inventoryRepository.findByFilmId(filmId);
    }

    public Page<Inventory> getInventoryByFilmId(Integer filmId, Pageable pageable) {
        return inventoryRepository.findByFilmId(filmId, pageable);
    }

    public List<Inventory> getInventoryByStoreId(Integer storeId) {
        return inventoryRepository.findByStoreId(storeId);
    }

    public Page<Inventory> getInventoryByStoreId(Integer storeId, Pageable pageable) {
        return inventoryRepository.findByStoreId(storeId, pageable);
    }

    public List<Inventory> getInventoryByFilmAndStore(Integer filmId, Integer storeId) {
        return inventoryRepository.findByFilmIdAndStoreId(filmId, storeId);
    }

    @Transactional
    public Inventory createInventory(Inventory inventory) {
        return inventoryRepository.save(inventory);
    }

    @Transactional
    public Inventory updateInventory(Integer id, Inventory updatedInventory) {
        Inventory existingInventory = getInventoryById(id);

        existingInventory.setFilmId(updatedInventory.getFilmId());
        existingInventory.setStoreId(updatedInventory.getStoreId());

        return inventoryRepository.save(existingInventory);
    }

    @Transactional
    public void deleteInventory(Integer id) {
        Inventory inventory = getInventoryById(id);

        // Check if inventory has active rentals
        if (inventory.getRentals() != null &&
                inventory.getRentals().stream().anyMatch(rental -> rental.getReturnDate() == null)) {
            throw new IllegalStateException("Cannot delete inventory with active rentals");
        }

        inventoryRepository.deleteById(id);
    }

    // Inventory count methods
    public Long getInventoryCountByFilmId(Integer filmId) {
        return inventoryRepository.countByFilmId(filmId);
    }

    public Long getInventoryCountByStoreId(Integer storeId) {
        return inventoryRepository.countByStoreId(storeId);
    }

    public Long getInventoryCountByFilmAndStore(Integer filmId, Integer storeId) {
        return inventoryRepository.countByFilmIdAndStoreId(filmId, storeId);
    }

    // Availability methods
    public List<Inventory> getAvailableInventory() {
        return inventoryRepository.findAvailableInventory();
    }

    public List<Inventory> getAvailableInventoryByFilmId(Integer filmId) {
        return inventoryRepository.findAvailableInventoryByFilmId(filmId);
    }

    public List<Inventory> getAvailableInventoryByStoreId(Integer storeId) {
        return inventoryRepository.findAvailableInventoryByStoreId(storeId);
    }

    public boolean isInventoryAvailable(Integer inventoryId) {
        return getAvailableInventory().stream()
                .anyMatch(inventory -> inventory.getInventoryId().equals(inventoryId));
    }
}