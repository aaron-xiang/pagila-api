package com.example.pagila_api.repository;

import com.example.pagila_api.integration.BaseIntegrationTest;
import com.example.pagila_api.model.Film;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class FilmRepositoryTest extends BaseIntegrationTest {

    @Autowired
    private FilmRepository filmRepository;

    @Test
    void testCreateAndFindFilm() {
        Film film = new Film();
        film.setTitle("Test Film");
        film.setDescription("A test film");
        film.setReleaseYear(2024);
        film.setLanguageId(1);
        film.setReplacementCost(new BigDecimal(2));
        film.setRentalRate(new BigDecimal(1));
        film.setRentalDuration(3);
        Film saved = filmRepository.save(film);

        assertThat(saved.getFilmId()).isNotNull();

        Optional<Film> found = filmRepository.findById(saved.getFilmId());
        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("Test Film");
    }

    @Test
    void testFindAllFilms() {
        List<Film> films = filmRepository.findAll();
        assertThat(films).isNotNull();
    }

    @Test
    void testUpdateFilm() {
        Film film = new Film();
        film.setTitle("Old Title");
        film.setDescription("Old Desc");
        film.setReleaseYear(2020);
        film.setLanguageId(1);
        film.setReplacementCost(new BigDecimal(2));
        film.setRentalRate(new BigDecimal(1));
        film.setRentalDuration(3);
        Film saved = filmRepository.save(film);

        saved.setTitle("New Title");
        Film updated = filmRepository.save(saved);

        assertThat(updated.getTitle()).isEqualTo("New Title");
    }

    @Test
    void testDeleteFilm() {
        Film film = new Film();
        film.setTitle("To Delete");
        film.setDescription("Desc");
        film.setReleaseYear(2021);
        film.setReleaseYear(2020);
        film.setLanguageId(1);
        film.setReplacementCost(new BigDecimal(2));
        film.setRentalRate(new BigDecimal(1));
        film.setRentalDuration(3);
        Film saved = filmRepository.save(film);

        filmRepository.deleteById(saved.getFilmId());
        Optional<Film> deleted = filmRepository.findById(saved.getFilmId());
        assertThat(deleted).isEmpty();
    }
}