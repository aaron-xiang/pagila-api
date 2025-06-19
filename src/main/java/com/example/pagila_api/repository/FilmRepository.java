package com.example.pagila_api.repository;

import com.example.pagila_api.model.Film;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FilmRepository extends JpaRepository<Film, Long> {
    // Add custom query methods if needed
}
