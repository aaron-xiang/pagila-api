package com.example.pagila_api.controller;

import com.example.pagila_api.integration.BaseIntegrationTest;
import com.example.pagila_api.model.Actor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;

class ActorControllerTest extends BaseIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateActor() {
        // CREATE
        Actor actor = new Actor();
        actor.setFirstName("API");
        actor.setLastName("Create");

        ResponseEntity<Actor> response = restTemplate.postForEntity("/api/actors", actor, Actor.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Actor created = response.getBody();
        assertThat(created).isNotNull();
        assertThat(created.getActorId()).isNotNull();
        assertThat(created.getFirstName()).isEqualTo("API");
        assertThat(created.getLastName()).isEqualTo("Create");
    }

    @Test
    void testGetActorById() {
        // CREATE first
        Actor actor = new Actor();
        actor.setFirstName("API");
        actor.setLastName("Get");
        ResponseEntity<Actor> createResponse = restTemplate.postForEntity("/api/actors", actor, Actor.class);
        Actor created = createResponse.getBody();

        // READ
        ResponseEntity<Actor> getResponse = restTemplate.getForEntity(
                "/api/actors/" + created.getActorId(), Actor.class);

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Actor found = getResponse.getBody();
        assertThat(found).isNotNull();
        assertThat(found.getActorId()).isEqualTo(created.getActorId());
        assertThat(found.getFirstName()).isEqualTo("API");
        assertThat(found.getLastName()).isEqualTo("Get");
    }

    @Test
    void testGetAllActors() {
        // CREATE multiple
        Actor actor1 = new Actor();
        actor1.setFirstName("API");
        actor1.setLastName("List1");
        restTemplate.postForEntity("/api/actors", actor1, Actor.class);

        Actor actor2 = new Actor();
        actor2.setFirstName("API");
        actor2.setLastName("List2");
        restTemplate.postForEntity("/api/actors", actor2, Actor.class);

        // READ all
        ResponseEntity<Actor[]> response = restTemplate.getForEntity("/api/actors", Actor[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Actor[] actors = response.getBody();
        assertThat(actors).isNotNull();
        assertThat(actors.length).isGreaterThanOrEqualTo(2);
    }

    @Test
    void testUpdateActor() {
        // CREATE
        Actor actor = new Actor();
        actor.setFirstName("API");
        actor.setLastName("Update");
        ResponseEntity<Actor> createResponse = restTemplate.postForEntity("/api/actors", actor, Actor.class);
        Actor created = createResponse.getBody();

        // UPDATE
        created.setFirstName("API");
        created.setLastName("Updated");

        HttpEntity<Actor> updateEntity = new HttpEntity<>(created);
        ResponseEntity<Actor> updateResponse = restTemplate.exchange(
                "/api/actors/" + created.getActorId(),
                HttpMethod.PUT,
                updateEntity,
                Actor.class);

        // VERIFY
        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Actor updated = updateResponse.getBody();
        assertThat(updated).isNotNull();
        assertThat(updated.getActorId()).isEqualTo(created.getActorId());
        assertThat(updated.getFirstName()).isEqualTo("API");
        assertThat(updated.getLastName()).isEqualTo("Updated");
    }

    @Test
    void testDeleteActor() {
        // CREATE
        Actor actor = new Actor();
        actor.setFirstName("API");
        actor.setLastName("Delete");
        ResponseEntity<Actor> createResponse = restTemplate.postForEntity("/api/actors", actor, Actor.class);
        Actor created = createResponse.getBody();

        // DELETE
        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                "/api/actors/" + created.getActorId(),
                HttpMethod.DELETE,
                null,
                Void.class);

        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        // VERIFY deletion
        ResponseEntity<String> getResponse = restTemplate.getForEntity(
                "/api/actors/" + created.getActorId(), String.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testGetNonExistentActor() {
        // READ non-existent
        ResponseEntity<String> response = restTemplate.getForEntity("/api/actors/99999", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testUpdateNonExistentActor() {
        // UPDATE non-existent
        Actor actor = new Actor();
        actor.setFirstName("Non");
        actor.setLastName("Existent");

        HttpEntity<Actor> updateEntity = new HttpEntity<>(actor);
        ResponseEntity<String> response = restTemplate.exchange(
                "/api/actors/99999",
                HttpMethod.PUT,
                updateEntity,
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testDeleteNonExistentActor() {
        // DELETE non-existent
        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/actors/99999",
                HttpMethod.DELETE,
                null,
                Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

//    @Test
//    void testCreateActorWithInvalidData() {
//        // CREATE with invalid data (assuming validation exists)
//        Actor actor = new Actor();
//        // Leave firstName and lastName null or empty
//
//        ResponseEntity<String> response = restTemplate.postForEntity("/api/actors", actor, String.class);
//
//        // This should return 400 Bad Request if validation is implemented
//        assertThat(response.getStatusCode()).isIn(HttpStatus.BAD_REQUEST, HttpStatus.UNPROCESSABLE_ENTITY);
//    }
}