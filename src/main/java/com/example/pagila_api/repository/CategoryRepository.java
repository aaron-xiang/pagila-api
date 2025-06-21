package com.example.pagila_api.repository;

import com.example.pagila_api.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    @Query("SELECT c FROM Category c JOIN FETCH c.films WHERE c.categoryId = :categoryId")
    Optional<Category> findByIdWithFilms(@Param("categoryId") Integer categoryId);

    Optional<Category> findByNameIgnoreCase(String name);

    Page<Category> findByNameContainingIgnoreCase(String name, Pageable pageable);

    @Query("SELECT c FROM Category c WHERE SIZE(c.films) > :filmCount")
    List<Category> findCategoriesWithMoreThanXFilms(@Param("filmCount") int filmCount);

    @Query("SELECT c FROM Category c ORDER BY SIZE(c.films) DESC")
    List<Category> findCategoriesOrderByFilmCount();

    boolean existsByNameIgnoreCase(String name);
}