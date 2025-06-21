package com.example.pagila_api.service;

import com.example.pagila_api.exception.ResourceNotFoundException;
import com.example.pagila_api.model.Rental;
import com.example.pagila_api.model.Inventory;
import com.example.pagila_api.repository.RentalRepository;
import com.example.pagila_api.repository.InventoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class RentalService {

    private final RentalRepository rentalRepository;
    private final InventoryRepository inventoryRepository;

    public RentalService(RentalRepository rentalRepository, InventoryRepository inventoryRepository) {
        this.rentalRepository = rentalRepository;
        this.inventoryRepository = inventoryRepository;
    }

    public Page<Rental> getAllRentals(Pageable pageable) {
        return rentalRepository.findAll(pageable);
    }

    public List<Rental> getAllRentals() {
        return rentalRepository.findAll();
    }

    public Rental getRentalById(Integer id) {
        return rentalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rental not found with ID: " + id));
    }

    public Rental getRentalByIdWithDetails(Integer id) {
        return rentalRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rental not found with ID: " + id));
    }

    // Active rentals
    public List<Rental> getActiveRentals() {
        return rentalRepository.findActiveRentals();
    }

    public Page<Rental> getActiveRentals(Pageable pageable) {
        return rentalRepository.findActiveRentals(pageable);
    }

    // Customer rentals
    public List<Rental> getRentalsByCustomerId(Integer customerId) {
        return rentalRepository.findByCustomerId(customerId);
    }

    public Page<Rental> getRentalsByCustomerId(Integer customerId, Pageable pageable) {
        return rentalRepository.findByCustomerId(customerId, pageable);
    }

    public List<Rental> getActiveRentalsByCustomerId(Integer customerId) {
        return rentalRepository.findActiveRentalsByCustomerId(customerId);
    }

    // Staff rentals
    public List<Rental> getRentalsByStaffId(Integer staffId) {
        return rentalRepository.findByStaffId(staffId);
    }

    public Page<Rental> getRentalsByStaffId(Integer staffId, Pageable pageable) {
        return rentalRepository.findByStaffId(staffId, pageable);
    }

    // Inventory rentals
    public List<Rental> getRentalsByInventoryId(Integer inventoryId) {
        return rentalRepository.findByInventoryId(inventoryId);
    }

    @Transactional
    public Rental createRental(Rental rental) {
        // Check if inventory is available
        if (rentalRepository.findActiveRentalByInventoryId(rental.getInventoryId()).isPresent()) {
            throw new IllegalStateException("Inventory item is currently rented out");
        }

        // Verify inventory exists
        if (!inventoryRepository.existsById(rental.getInventoryId())) {
            throw new ResourceNotFoundException("Inventory not found with ID: " + rental.getInventoryId());
        }

        // Set rental date if not provided
        if (rental.getRentalDate() == null) {
            rental.setRentalDate(LocalDateTime.now());
        }

        return rentalRepository.save(rental);
    }

    @Transactional
    public Rental updateRental(Integer id, Rental updatedRental) {
        Rental existingRental = getRentalById(id);

        // Only allow updating certain fields
        existingRental.setReturnDate(updatedRental.getReturnDate());
        existingRental.setStaffId(updatedRental.getStaffId());

        return rentalRepository.save(existingRental);
    }

    @Transactional
    public Rental returnRental(Integer rentalId) {
        Rental rental = getRentalById(rentalId);

        if (rental.getReturnDate() != null) {
            throw new IllegalStateException("Rental has already been returned");
        }

        rental.setReturnDate(LocalDateTime.now());
        return rentalRepository.save(rental);
    }

    @Transactional
    public void deleteRental(Integer id) {
        Rental rental = getRentalById(id);

        // Check if rental has associated payments
        if (rental.getPayments() != null && !rental.getPayments().isEmpty()) {
            throw new IllegalStateException("Cannot delete rental with associated payments");
        }

        rentalRepository.deleteById(id);
    }

    // Date range queries
    public List<Rental> getRentalsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return rentalRepository.findByRentalDateBetween(startDate, endDate);
    }

    public Page<Rental> getRentalsByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        return rentalRepository.findByRentalDateBetween(startDate, endDate, pageable);
    }

    public List<Rental> getReturnsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return rentalRepository.findByReturnDateBetween(startDate, endDate);
    }

    // Overdue rentals
    public List<Rental> getOverdueRentals() {
        // Assuming 7 days default rental period
        LocalDateTime overdueDate = LocalDateTime.now().minusDays(7);
        return rentalRepository.findOverdueRentals(overdueDate);
    }

    public List<Rental> getOverdueRentalsBasedOnFilmDuration() {
        return rentalRepository.findOverdueRentalsBasedOnFilmDuration();
    }

    // Statistics
    public Long getRentalCountByCustomerId(Integer customerId) {
        return rentalRepository.countRentalsByCustomerId(customerId);
    }

    public Long getRentalCountByStaffId(Integer staffId) {
        return rentalRepository.countRentalsByStaffId(staffId);
    }

    public Long getActiveRentalCount() {
        return rentalRepository.countActiveRentals();
    }

    public Long getActiveRentalCountByCustomerId(Integer customerId) {
        return rentalRepository.countActiveRentalsByCustomerId(customerId);
    }

    // Business intelligence
    public List<Object[]> getTopCustomersByRentalCount() {
        return rentalRepository.findTopCustomersByRentalCount();
    }

    public List<Object[]> getMostRentedFilms() {
        return rentalRepository.findMostRentedFilms();
    }

    public List<Object[]> getRentalCountByStore() {
        return rentalRepository.findRentalCountByStore();
    }

    public List<Rental> getRentalsWithPaymentsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return rentalRepository.findRentalsWithPaymentsBetweenDates(startDate, endDate);
    }

    // Availability check
    public boolean isInventoryAvailable(Integer inventoryId) {
        return rentalRepository.findActiveRentalByInventoryId(inventoryId).isEmpty();
    }
}