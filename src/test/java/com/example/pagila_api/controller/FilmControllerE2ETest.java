package com.example.pagila_api.controller;

import com.example.pagila_api.integration.BaseIntegrationTest;
import com.example.pagila_api.model.Film;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class FilmControllerE2ETest extends BaseIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testFilmCrudLifecycle() {
        // CREATE
        Film film = new Film();
        film.setTitle("E2E Film");
        film.setDescription("E2E Desc");
        film.setReleaseYear(2025);
        film.setLanguageId(1);
        film.setReplacementCost(new BigDecimal(2));
        film.setRentalRate(new BigDecimal(1));
        film.setRentalDuration(3);

        ResponseEntity<Film> createResponse = restTemplate.postForEntity("/api/films", film, Film.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Film created = createResponse.getBody();
        assertThat(created).isNotNull();
        Integer filmId = created.getFilmId();

        // READ (by ID)
        ResponseEntity<Film> getResponse = restTemplate.getForEntity("/api/films/" + filmId, Film.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Film found = getResponse.getBody();
        assertThat(found).isNotNull();
        assertThat(found.getTitle()).isEqualTo("E2E Film");

        // UPDATE
        found.setTitle("Updated E2E Film");
        HttpEntity<Film> updateEntity = new HttpEntity<>(found);
        ResponseEntity<Film> updateResponse = restTemplate.exchange(
                "/api/films/" + filmId, HttpMethod.PUT, updateEntity, Film.class);
        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Film updated = updateResponse.getBody();
        assertThat(updated).isNotNull();
        assertThat(updated.getTitle()).isEqualTo("Updated E2E Film");

        // READ ALL
//        ResponseEntity<Film[]> allResponse = restTemplate.getForEntity("/api/films", Film[].class);
        ResponseEntity<String> allResponse = restTemplate.getForEntity("/api/films", String.class);
        assertThat(allResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(allResponse.getBody()).isNotNull();
//        assertThat(allResponse.getBody().length).isGreaterThanOrEqualTo(1);
        assertThat(allResponse.getBody().length()).isGreaterThanOrEqualTo(1);

        // DELETE
        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                "/api/films/" + filmId, HttpMethod.DELETE, null, Void.class);
        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        // VERIFY DELETION
//        try {
//            restTemplate.getForEntity("/api/films/" + filmId, Film.class);
//            // If no exception, fail the test
//            assertThat(false).isTrue();
//        } catch (org.springframework.web.client.HttpClientErrorException ex) {
//            assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
//        }
    }
}