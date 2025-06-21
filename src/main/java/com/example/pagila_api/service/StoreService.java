package com.example.pagila_api.service;

import com.example.pagila_api.exception.ResourceNotFoundException;
import com.example.pagila_api.model.Store;
import com.example.pagila_api.repository.StoreRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class StoreService {

    private final StoreRepository storeRepository;

    public StoreService(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    public Page<Store> getAllStores(Pageable pageable) {
        return storeRepository.findAll(pageable);
    }

    public List<Store> getAllStores() {
        return storeRepository.findAll();
    }

    public List<Store> getAllStoresWithDetails() {
        return storeRepository.findAllWithDetails();
    }

    public Store getStoreById(Integer id) {
        return storeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with ID: " + id));
    }

    public Store getStoreByIdWithAddress(Integer id) {
        return storeRepository.findByIdWithAddress(id)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with ID: " + id));
    }

    public Store getStoreByIdWithManager(Integer id) {
        return storeRepository.findByIdWithManager(id)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with ID: " + id));
    }

    public Store getStoreByIdWithAddressAndManager(Integer id) {
        return storeRepository.findByIdWithAddressAndManager(id)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with ID: " + id));
    }

    public Store getStoreByManagerId(Integer managerStaffId) {
        return storeRepository.findByManagerStaffId(managerStaffId)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with manager ID: " + managerStaffId));
    }

    public List<Store> getStoresByAddressId(Integer addressId) {
        return storeRepository.findByAddressId(addressId);
    }

    @Transactional
    public Store createStore(Store store) {
        // Validate that manager staff ID exists and is not already managing another store
        if (storeRepository.findByManagerStaffId(store.getManagerStaffId()).isPresent()) {
            throw new IllegalArgumentException("Staff member is already managing another store");
        }

        return storeRepository.save(store);
    }

    @Transactional
    public Store updateStore(Integer id, Store updatedStore) {
        Store existingStore = getStoreById(id);

        // Check if the new manager is not already managing another store (excluding current store)
        storeRepository.findByManagerStaffId(updatedStore.getManagerStaffId())
                .ifPresent(store -> {
                    if (!store.getStoreId().equals(id)) {
                        throw new IllegalArgumentException("Staff member is already managing another store");
                    }
                });

        existingStore.setManagerStaffId(updatedStore.getManagerStaffId());
        existingStore.setAddressId(updatedStore.getAddressId());

        return storeRepository.save(existingStore);
    }

    @Transactional
    public void deleteStore(Integer id) {
        Store store = getStoreById(id);

        // Check if store has active business (customers, staff, inventory)
        Long customerCount = storeRepository.countCustomersByStoreId(id);
        Long staffCount = storeRepository.countStaffByStoreId(id);
        Long inventoryCount = storeRepository.countInventoryByStoreId(id);

        if (customerCount > 0 || staffCount > 1 || inventoryCount > 0) { // staffCount > 1 because manager is also staff
            throw new IllegalStateException("Cannot delete store with active customers, staff, or inventory");
        }

        storeRepository.deleteById(id);
    }

    // Analytics and reporting methods
    public Long getInventoryCountByStoreId(Integer storeId) {
        return storeRepository.countInventoryByStoreId(storeId);
    }

    public Long getCustomerCountByStoreId(Integer storeId) {
        return storeRepository.countCustomersByStoreId(storeId);
    }

    public Long getStaffCountByStoreId(Integer storeId) {
        return storeRepository.countStaffByStoreId(storeId);
    }

    // Geographic queries
    public List<Store> getStoresByCity(String cityName) {
        return storeRepository.findByCity(cityName);
    }

    public List<Store> getStoresByCountry(String countryName) {
        return storeRepository.findByCountry(countryName);
    }

    // Performance metrics
    public List<Store> getStoresWithMoreThanXRentals(Long rentalCount) {
        return storeRepository.findStoresWithMoreThanXRentals(rentalCount);
    }

    // Business intelligence methods
    public StoreStats getStoreStats(Integer storeId) {
        Store store = getStoreById(storeId);
        Long inventoryCount = getInventoryCountByStoreId(storeId);
        Long customerCount = getCustomerCountByStoreId(storeId);
        Long staffCount = getStaffCountByStoreId(storeId);

        return new StoreStats(store, inventoryCount, customerCount, staffCount);
    }

    // Inner class for store statistics
    public static class StoreStats {
        private final Store store;
        private final Long inventoryCount;
        private final Long customerCount;
        private final Long staffCount;

        public StoreStats(Store store, Long inventoryCount, Long customerCount, Long staffCount) {
            this.store = store;
            this.inventoryCount = inventoryCount;
            this.customerCount = customerCount;
            this.staffCount = staffCount;
        }

        // Getters
        public Store getStore() { return store; }
        public Long getInventoryCount() { return inventoryCount; }
        public Long getCustomerCount() { return customerCount; }
        public Long getStaffCount() { return staffCount; }
    }
}