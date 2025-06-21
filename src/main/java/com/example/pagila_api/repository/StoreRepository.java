package com.example.pagila_api.repository;

import com.example.pagila_api.model.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Integer> {

    @Query("SELECT s FROM Store s JOIN FETCH s.address WHERE s.storeId = :storeId")
    Optional<Store> findByIdWithAddress(@Param("storeId") Integer storeId);

    @Query("SELECT s FROM Store s JOIN FETCH s.managerStaff WHERE s.storeId = :storeId")
    Optional<Store> findByIdWithManager(@Param("storeId") Integer storeId);

    @Query("SELECT s FROM Store s " +
            "JOIN FETCH s.address " +
            "JOIN FETCH s.managerStaff " +
            "WHERE s.storeId = :storeId")
    Optional<Store> findByIdWithAddressAndManager(@Param("storeId") Integer storeId);

    @Query("SELECT s FROM Store s " +
            "JOIN FETCH s.address " +
            "JOIN FETCH s.managerStaff " +
            "LEFT JOIN FETCH s.inventories")
    List<Store> findAllWithDetails();

    Optional<Store> findByManagerStaffId(Integer managerStaffId);

    List<Store> findByAddressId(Integer addressId);

    @Query("SELECT COUNT(i) FROM Inventory i WHERE i.storeId = :storeId")
    Long countInventoryByStoreId(@Param("storeId") Integer storeId);

    @Query("SELECT COUNT(c) FROM Customer c WHERE c.storeId = :storeId")
    Long countCustomersByStoreId(@Param("storeId") Integer storeId);

    @Query("SELECT COUNT(s) FROM Staff s WHERE s.storeId = :storeId")
    Long countStaffByStoreId(@Param("storeId") Integer storeId);

    // Find stores by city
    @Query("SELECT s FROM Store s " +
            "JOIN s.address a " +
            "JOIN a.city c " +
            "WHERE c.city = :cityName")
    List<Store> findByCity(@Param("cityName") String cityName);

    // Find stores by country
    @Query("SELECT s FROM Store s " +
            "JOIN s.address a " +
            "JOIN a.city c " +
            "JOIN c.country co " +
            "WHERE co.country = :countryName")
    List<Store> findByCountry(@Param("countryName") String countryName);

    // Store performance metrics
    @Query("SELECT s FROM Store s " +
            "WHERE s.storeId IN (" +
            "SELECT r.staff.storeId FROM Rental r " +
            "GROUP BY r.staff.storeId " +
            "HAVING COUNT(r) > :rentalCount)")
    List<Store> findStoresWithMoreThanXRentals(@Param("rentalCount") Long rentalCount);
}