package com.example.pagila_api.service;

import com.example.pagila_api.exception.ResourceNotFoundException;
import com.example.pagila_api.integration.BaseIntegrationTest;
import com.example.pagila_api.model.Film;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FilmServiceTest extends BaseIntegrationTest {

    @Autowired
    private FilmService filmService;

    @Test
    void testCreateAndGetFilm() {
        Film film = new Film();
        film.setTitle("Service Film");
        film.setDescription("Service Desc");
        film.setReleaseYear(2022);
        film.setLanguageId(1);
        film.setReplacementCost(new BigDecimal(2));
        film.setRentalRate(new BigDecimal(1));
        film.setRentalDuration(3);
        Film created = filmService.createFilm(film);

        assertThat(created.getFilmId()).isNotNull();

        Film found = filmService.getFilmById(created.getFilmId());
        assertThat(found).isNotNull();
        assertThat(found.getTitle()).isEqualTo("Service Film");
    }

    @Test
    void testGetAllFilms() {
        List<Film> films = filmService.getAllFilms();
        assertThat(films).isNotNull();
    }

    @Test
    void testUpdateFilm() {
        Film film = new Film();
        film.setTitle("Update Me");
        film.setDescription("Desc");
        film.setReleaseYear(2023);
        film.setLanguageId(1);
        film.setReplacementCost(new BigDecimal(2));
        film.setRentalRate(new BigDecimal(1));
        film.setRentalDuration(3);
        Film created = filmService.createFilm(film);

        Film updateData = new Film();
        updateData.setTitle("Updated Title");
        updateData.setDescription("Updated Desc");
        updateData.setReleaseYear(2024);
        updateData.setLanguageId(2);
        updateData.setReplacementCost(new BigDecimal(3));
        updateData.setRentalRate(new BigDecimal(2));
        updateData.setRentalDuration(2);

        Film updated = filmService.updateFilm(created.getFilmId(), updateData);

        assertThat(updated.getTitle()).isEqualTo("Updated Title");
        assertThat(updated.getDescription()).isEqualTo("Updated Desc");
        assertThat(updated.getReleaseYear()).isEqualTo(2024);
    }

    @Test
    void testDeleteFilm() {
        Film film = new Film();
        film.setTitle("Delete Me");
        film.setDescription("Desc");
        film.setReleaseYear(2022);
        film.setLanguageId(1);
        film.setReplacementCost(new BigDecimal(2));
        film.setRentalRate(new BigDecimal(1));
        film.setRentalDuration(3);
        Film created = filmService.createFilm(film);

        filmService.deleteFilm(created.getFilmId());

        assertThatThrownBy(() -> filmService.getFilmById(created.getFilmId()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Film not found with ID: " + created.getFilmId());
    }

    @Test
    void testGetNonExistentFilm() {
        assertThatThrownBy(() -> filmService.getFilmById(99999))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Film not found with ID: 99999");
    }
}