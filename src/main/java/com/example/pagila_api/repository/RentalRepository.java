package com.example.pagila_api.repository;

import com.example.pagila_api.model.Rental;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Integer> {

    @Query("SELECT r FROM Rental r " +
            "JOIN FETCH r.inventory i " +
            "JOIN FETCH i.film " +
            "JOIN FETCH r.customer " +
            "JOIN FETCH r.staff " +
            "WHERE r.rentalId = :rentalId")
    Optional<Rental> findByIdWithDetails(@Param("rentalId") Integer rentalId);

    // Active rentals (not returned)
    @Query("SELECT r FROM Rental r WHERE r.returnDate IS NULL")
    List<Rental> findActiveRentals();

    @Query("SELECT r FROM Rental r WHERE r.returnDate IS NULL")
    Page<Rental> findActiveRentals(Pageable pageable);

    // Customer rentals
    List<Rental> findByCustomerId(Integer customerId);

    Page<Rental> findByCustomerId(Integer customerId, Pageable pageable);

    @Query("SELECT r FROM Rental r WHERE r.customer.customerId = :customerId AND r.returnDate IS NULL")
    List<Rental> findActiveRentalsByCustomerId(@Param("customerId") Integer customerId);

    // Staff rentals
    List<Rental> findByStaffId(Integer staffId);

    Page<Rental> findByStaffId(Integer staffId, Pageable pageable);

    // Inventory rentals
    List<Rental> findByInventoryId(Integer inventoryId);

    @Query("SELECT r FROM Rental r WHERE r.inventory.inventoryId = :inventoryId AND r.returnDate IS NULL")
    Optional<Rental> findActiveRentalByInventoryId(@Param("inventoryId") Integer inventoryId);

    // Date range queries
    List<Rental> findByRentalDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    Page<Rental> findByRentalDateBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    List<Rental> findByReturnDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    // Overdue rentals
    @Query("SELECT r FROM Rental r " +
            "JOIN r.inventory i " +
            "JOIN i.film f " +
            "WHERE r.returnDate IS NULL " +
            "AND r.rentalDate < :overdueDate")
    List<Rental> findOverdueRentals(@Param("overdueDate") LocalDateTime overdueDate);

    @Query("SELECT r FROM Rental r " +
            "JOIN FETCH r.inventory i " +
            "JOIN FETCH i.film f " +
            "WHERE r.returnDate IS NULL")
    List<Rental> findOverdueRentalsBasedOnFilmDuration();

    // Statistics queries
    @Query("SELECT COUNT(r) FROM Rental r WHERE r.customer.customerId = :customerId")
    Long countRentalsByCustomerId(@Param("customerId") Integer customerId);

    @Query("SELECT COUNT(r) FROM Rental r WHERE r.staff.staffId = :staffId")
    Long countRentalsByStaffId(@Param("staffId") Integer staffId);

    @Query("SELECT COUNT(r) FROM Rental r WHERE r.returnDate IS NULL")
    Long countActiveRentals();

    @Query("SELECT COUNT(r) FROM Rental r WHERE r.customer.customerId = :customerId AND r.returnDate IS NULL")
    Long countActiveRentalsByCustomerId(@Param("customerId") Integer customerId);

    // Top customers
    @Query("SELECT r.customer.customerId, COUNT(r) as rentalCount FROM Rental r " +
            "GROUP BY r.customer.customerId " +
            "ORDER BY rentalCount DESC")
    List<Object[]> findTopCustomersByRentalCount();

    // Revenue related
    @Query("SELECT r FROM Rental r " +
            "JOIN FETCH r.payments " +
            "WHERE r.rentalDate BETWEEN :startDate AND :endDate")
    List<Rental> findRentalsWithPaymentsBetweenDates(@Param("startDate") LocalDateTime startDate,
                                                     @Param("endDate") LocalDateTime endDate);

    // Film popularity
    @Query("SELECT i.film.filmId, COUNT(r) as rentalCount FROM Rental r " +
            "JOIN r.inventory i " +
            "GROUP BY i.film.filmId " +
            "ORDER BY rentalCount DESC")
    List<Object[]> findMostRentedFilms();

    // Store performance
    @Query("SELECT s.store.storeId, COUNT(r) as rentalCount FROM Rental r " +
            "JOIN r.staff s " +
            "GROUP BY s.store.storeId " +
            "ORDER BY rentalCount DESC")
    List<Object[]> findRentalCountByStore();
}