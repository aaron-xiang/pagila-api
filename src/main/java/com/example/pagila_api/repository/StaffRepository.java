package com.example.pagila_api.repository;

import com.example.pagila_api.model.Staff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Integer> {

    @Query("SELECT s FROM Staff s JOIN FETCH s.address WHERE s.staffId = :staffId")
    Optional<Staff> findByIdWithAddress(@Param("staffId") Integer staffId);

    @Query("SELECT s FROM Staff s JOIN FETCH s.store WHERE s.staffId = :staffId")
    Optional<Staff> findByIdWithStore(@Param("staffId") Integer staffId);

    @Query("SELECT s FROM Staff s " +
            "JOIN FETCH s.address " +
            "JOIN FETCH s.store " +
            "WHERE s.staffId = :staffId")
    Optional<Staff> findByIdWithDetails(@Param("staffId") Integer staffId);

    // Authentication
    Optional<Staff> findByUsername(String username);

    Optional<Staff> findByUsernameAndPassword(String username, String password);

    Optional<Staff> findByEmail(String email);

    // Active staff
    List<Staff> findByActiveTrue();

    Page<Staff> findByActiveTrue(Pageable pageable);

    List<Staff> findByActiveFalse();

    // Store-based queries
    List<Staff> findByStoreId(Integer storeId);

    Page<Staff> findByStoreId(Integer storeId, Pageable pageable);

    List<Staff> findByStoreIdAndActiveTrue(Integer storeId);

    // Name-based searches
    List<Staff> findByFirstNameContainingIgnoreCase(String firstName);

    List<Staff> findByLastNameContainingIgnoreCase(String lastName);

    @Query("SELECT s FROM Staff s WHERE " +
            "LOWER(s.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR " +
            "LOWER(s.lastName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Staff> findByNameContaining(@Param("name") String name);

    Page<Staff> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
            String firstName, String lastName, Pageable pageable);

    // Managers
    @Query("SELECT s FROM Staff s WHERE s.staffId IN " +
            "(SELECT st.managerStaffId FROM Store st)")
    List<Staff> findManagers();

    @Query("SELECT s FROM Staff s WHERE s.staffId NOT IN " +
            "(SELECT st.managerStaffId FROM Store st)")
    List<Staff> findNonManagers();

    // Performance queries
    @Query("SELECT COUNT(r) FROM Rental r WHERE r.staff.staffId = :staffId")
    Long countRentalsByStaffId(@Param("staffId") Integer staffId);

    @Query("SELECT COUNT(p) FROM Payment p WHERE p.staff.staffId = :staffId")
    Long countPaymentsByStaffId(@Param("staffId") Integer staffId);

    @Query("SELECT COUNT(r) FROM Rental r WHERE r.staff.staffId = :staffId AND r.rentalDate BETWEEN :startDate AND :endDate")
    Long countRentalsByStaffIdAndDateRange(@Param("staffId") Integer staffId,
                                           @Param("startDate") LocalDateTime startDate,
                                           @Param("endDate") LocalDateTime endDate);

    // Top performers
    @Query("SELECT s.staffId, s.firstName, s.lastName, COUNT(r) as rentalCount FROM Staff s " +
            "LEFT JOIN s.rentals r " +
            "GROUP BY s.staffId, s.firstName, s.lastName " +
            "ORDER BY rentalCount DESC")
    List<Object[]> findStaffByRentalCount();

    @Query("SELECT s.staffId, s.firstName, s.lastName, COUNT(p) as paymentCount FROM Staff s " +
            "LEFT JOIN s.payments p " +
            "GROUP BY s.staffId, s.firstName, s.lastName " +
            "ORDER BY paymentCount DESC")
    List<Object[]> findStaffByPaymentCount();

    // Validation queries
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @Query("SELECT COUNT(s) > 0 FROM Staff s WHERE s.username = :username AND s.staffId != :staffId")
    boolean existsByUsernameAndStaffIdNot(@Param("username") String username, @Param("staffId") Integer staffId);

    @Query("SELECT COUNT(s) > 0 FROM Staff s WHERE s.email = :email AND s.staffId != :staffId")
    boolean existsByEmailAndStaffIdNot(@Param("email") String email, @Param("staffId") Integer staffId);
}