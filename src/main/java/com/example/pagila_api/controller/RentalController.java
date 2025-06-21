package com.example.pagila_api.controller;

import com.example.pagila_api.model.Rental;
import com.example.pagila_api.service.RentalService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    private final RentalService rentalService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @GetMapping
    public Page<Rental> getAllRentals(Pageable pageable) {
        return rentalService.getAllRentals(pageable);
    }

    @GetMapping("/all")
    public List<Rental> getAllRentalsAsList() {
        return rentalService.getAllRentals();
    }

    @GetMapping("/{id}")
    public Rental getRentalById(@PathVariable Integer id) {
        return rentalService.getRentalById(id);
    }

    @GetMapping("/{id}/with-details")
    public Rental getRentalByIdWithDetails(@PathVariable Integer id) {
        return rentalService.getRentalByIdWithDetails(id);
    }

    @GetMapping("/active")
    public List<Rental> getActiveRentals() {
        return rentalService.getActiveRentals();
    }

    @GetMapping("/active/paginated")
    public Page<Rental> getActiveRentalsPaginated(Pageable pageable) {
        return rentalService.getActiveRentals(pageable);
    }

    @GetMapping("/by-customer/{customerId}")
    public List<Rental> getRentalsByCustomerId(@PathVariable Integer customerId) {
        return rentalService.getRentalsByCustomerId(customerId);
    }

    @GetMapping("/by-customer/{customerId}/paginated")
    public Page<Rental> getRentalsByCustomerIdPaginated(@PathVariable Integer customerId, Pageable pageable) {
        return rentalService.getRentalsByCustomerId(customerId, pageable);
    }

    @GetMapping("/by-customer/{customerId}/active")
    public List<Rental> getActiveRentalsByCustomerId(@PathVariable Integer customerId) {
        return rentalService.getActiveRentalsByCustomerId(customerId);
    }

    @GetMapping("/by-staff/{staffId}")
    public List<Rental> getRentalsByStaffId(@PathVariable Integer staffId) {
        return rentalService.getRentalsByStaffId(staffId);
    }

    @GetMapping("/by-staff/{staffId}/paginated")
    public Page<Rental> getRentalsByStaffIdPaginated(@PathVariable Integer staffId, Pageable pageable) {
        return rentalService.getRentalsByStaffId(staffId, pageable);
    }

    @GetMapping("/by-inventory/{inventoryId}")
    public List<Rental> getRentalsByInventoryId(@PathVariable Integer inventoryId) {
        return rentalService.getRentalsByInventoryId(inventoryId);
    }

    @PostMapping
    public ResponseEntity<Rental> createRental(@Valid @RequestBody Rental rental) {
        Rental createdRental = rentalService.createRental(rental);
        return ResponseEntity.status(201).body(createdRental);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Rental> updateRental(@PathVariable Integer id, @Valid @RequestBody Rental rental) {
        Rental updatedRental = rentalService.updateRental(id, rental);
        return ResponseEntity.ok(updatedRental);
    }

    @PutMapping("/{id}/return")
    public ResponseEntity<Rental> returnRental(@PathVariable Integer id) {
        Rental returnedRental = rentalService.returnRental(id);
        return ResponseEntity.ok(returnedRental);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRental(@PathVariable Integer id) {
        rentalService.deleteRental(id);
        return ResponseEntity.noContent().build();
    }

    // Date range queries
    @GetMapping("/by-date-range")
    public List<Rental> getRentalsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return rentalService.getRentalsByDateRange(startDate, endDate);
    }

    @GetMapping("/by-date-range/paginated")
    public Page<Rental> getRentalsByDateRangePaginated(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            Pageable pageable) {
        return rentalService.getRentalsByDateRange(startDate, endDate, pageable);
    }

    @GetMapping("/returns-by-date-range")
    public List<Rental> getReturnsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return rentalService.getReturnsByDateRange(startDate, endDate);
    }

    // Overdue rentals
    @GetMapping("/overdue")
    public List<Rental> getOverdueRentals() {
        return rentalService.getOverdueRentals();
    }

    @GetMapping("/overdue/by-film-duration")
    public List<Rental> getOverdueRentalsBasedOnFilmDuration() {
        return rentalService.getOverdueRentalsBasedOnFilmDuration();
    }

    // Statistics endpoints
    @GetMapping("/count/by-customer/{customerId}")
    public ResponseEntity<Long> getRentalCountByCustomerId(@PathVariable Integer customerId) {
        Long count = rentalService.getRentalCountByCustomerId(customerId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/by-staff/{staffId}")
    public ResponseEntity<Long> getRentalCountByStaffId(@PathVariable Integer staffId) {
        Long count = rentalService.getRentalCountByStaffId(staffId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/active")
    public ResponseEntity<Long> getActiveRentalCount() {
        Long count = rentalService.getActiveRentalCount();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/active/by-customer/{customerId}")
    public ResponseEntity<Long> getActiveRentalCountByCustomerId(@PathVariable Integer customerId) {
        Long count = rentalService.getActiveRentalCountByCustomerId(customerId);
        return ResponseEntity.ok(count);
    }

    // Business intelligence endpoints
    @GetMapping("/analytics/top-customers")
    public List<Object[]> getTopCustomersByRentalCount() {
        return rentalService.getTopCustomersByRentalCount();
    }

    @GetMapping("/analytics/most-rented-films")
    public List<Object[]> getMostRentedFilms() {
        return rentalService.getMostRentedFilms();
    }

    @GetMapping("/analytics/rentals-by-store")
    public List<Object[]> getRentalCountByStore() {
        return rentalService.getRentalCountByStore();
    }

    @GetMapping("/with-payments/by-date-range")
    public List<Rental> getRentalsWithPaymentsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return rentalService.getRentalsWithPaymentsByDateRange(startDate, endDate);
    }

    // Availability check
    @GetMapping("/inventory/{inventoryId}/is-available")
    public ResponseEntity<Boolean> isInventoryAvailable(@PathVariable Integer inventoryId) {
        boolean isAvailable = rentalService.isInventoryAvailable(inventoryId);
        return ResponseEntity.ok(isAvailable);
    }
}