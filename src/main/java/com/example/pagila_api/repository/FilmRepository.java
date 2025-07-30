package com.example.pagila_api.repository;

import com.example.pagila_api.model.Film;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FilmRepository extends JpaRepository<Film, Integer> {

    @Query("SELECT f FROM Film f JOIN FETCH f.actors WHERE f.filmId = :filmId")
    Optional<Film> findByIdWithActors(@Param("filmId") Integer filmId);

    Page<Film> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    List<Film> findByReleaseYear(Integer releaseYear);

    @Query("SELECT f FROM Film f WHERE f.rating = :rating")
    Page<Film> findByRating(@Param("rating") Film.Rating rating, Pageable pageable);
}