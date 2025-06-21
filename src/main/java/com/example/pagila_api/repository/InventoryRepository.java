package com.example.pagila_api.repository;

import com.example.pagila_api.model.Inventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Integer> {

    @Query("SELECT i FROM Inventory i JOIN FETCH i.film WHERE i.inventoryId = :inventoryId")
    Optional<Inventory> findByIdWithFilm(@Param("inventoryId") Integer inventoryId);

    @Query("SELECT i FROM Inventory i JOIN FETCH i.film JOIN FETCH i.store WHERE i.inventoryId = :inventoryId")
    Optional<Inventory> findByIdWithFilmAndStore(@Param("inventoryId") Integer inventoryId);

    List<Inventory> findByFilmId(Integer filmId);

    List<Inventory> findByStoreId(Integer storeId);

    Page<Inventory> findByFilmId(Integer filmId, Pageable pageable);

    Page<Inventory> findByStoreId(Integer storeId, Pageable pageable);

    @Query("SELECT i FROM Inventory i WHERE i.filmId = :filmId AND i.storeId = :storeId")
    List<Inventory> findByFilmIdAndStoreId(@Param("filmId") Integer filmId, @Param("storeId") Integer storeId);

    @Query("SELECT COUNT(i) FROM Inventory i WHERE i.filmId = :filmId")
    Long countByFilmId(@Param("filmId") Integer filmId);

    @Query("SELECT COUNT(i) FROM Inventory i WHERE i.storeId = :storeId")
    Long countByStoreId(@Param("storeId") Integer storeId);

    @Query("SELECT COUNT(i) FROM Inventory i WHERE i.filmId = :filmId AND i.storeId = :storeId")
    Long countByFilmIdAndStoreId(@Param("filmId") Integer filmId, @Param("storeId") Integer storeId);

    // Find available inventory (not currently rented)
    @Query("SELECT i FROM Inventory i WHERE i.inventoryId NOT IN " +
            "(SELECT r.inventory.inventoryId FROM Rental r WHERE r.returnDate IS NULL)")
    List<Inventory> findAvailableInventory();

    @Query("SELECT i FROM Inventory i WHERE i.filmId = :filmId AND i.inventoryId NOT IN " +
            "(SELECT r.inventory.inventoryId FROM Rental r WHERE r.returnDate IS NULL)")
    List<Inventory> findAvailableInventoryByFilmId(@Param("filmId") Integer filmId);

    @Query("SELECT i FROM Inventory i WHERE i.storeId = :storeId AND i.inventoryId NOT IN " +
            "(SELECT r.inventory.inventoryId FROM Rental r WHERE r.returnDate IS NULL)")
    List<Inventory> findAvailableInventoryByStoreId(@Param("storeId") Integer storeId);
}