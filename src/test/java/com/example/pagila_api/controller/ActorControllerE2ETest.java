package com.example.pagila_api.controller;

import com.example.pagila_api.config.TestSecurityConfig;
import com.example.pagila_api.integration.BaseIntegrationTest;
import com.example.pagila_api.model.Actor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
//@Import(TestSecurityConfig.class)
class ActorControllerE2ETest extends BaseIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testActorCrudLifecycle() {
        // CREATE
        Actor actor = new Actor();
        actor.setFirstName("E2E");
        actor.setLastName("Test");

        ResponseEntity<Actor> createResponse = restTemplate.postForEntity("/api/actors", actor, Actor.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Actor created = createResponse.getBody();
        assertThat(created).isNotNull();
        Integer actorId = created.getActorId();

        // READ (by ID)
        ResponseEntity<Actor> getResponse = restTemplate.getForEntity("/api/actors/" + actorId, Actor.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Actor found = getResponse.getBody();
        assertThat(found).isNotNull();
        assertThat(found.getFirstName()).isEqualTo("E2E");
        assertThat(found.getLastName()).isEqualTo("Test");

        // UPDATE
        found.setLastName("Updated");
        HttpEntity<Actor> updateEntity = new HttpEntity<>(found);
        ResponseEntity<Actor> updateResponse = restTemplate.exchange(
                "/api/actors/" + actorId, HttpMethod.PUT, updateEntity, Actor.class);
        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Actor updated = updateResponse.getBody();
        assertThat(updated).isNotNull();
        assertThat(updated.getLastName()).isEqualTo("Updated");

        // READ ALL
        ResponseEntity<Actor[]> allResponse = restTemplate.getForEntity("/api/actors", Actor[].class);
        assertThat(allResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(allResponse.getBody()).isNotNull();
        assertThat(allResponse.getBody().length).isGreaterThanOrEqualTo(1);

        // DELETE
        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                "/api/actors/" + actorId, HttpMethod.DELETE, null, Void.class);
        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        // VERIFY DELETION
        ResponseEntity<String> getAfterDelete = restTemplate.getForEntity("/api/actors/" + actorId, String.class);
        assertThat(getAfterDelete.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

//        try {
//            restTemplate.getForEntity("/api/actors/" + actorId, Actor.class);
//            fail("Expected an HttpClientErrorException to be thrown");
//        } catch (org.springframework.web.client.HttpClientErrorException ex) {
//            System.out.println(ex.getStatusCode());
//            System.out.println(ex.getResponseBodyAsString());
//            assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
//        }
    }

    @Test
    void testActorNotFoundAndValidation() {
        // GET non-existent
        ResponseEntity<String> getResponse = restTemplate.getForEntity("/api/actors/99999", String.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        // UPDATE non-existent
        Actor actor = new Actor();
        actor.setFirstName("Ghost");
        actor.setLastName("Actor");
        HttpEntity<Actor> updateEntity = new HttpEntity<>(actor);
        ResponseEntity<String> updateResponse = restTemplate.exchange(
                "/api/actors/99999", HttpMethod.PUT, updateEntity, String.class);
        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        // DELETE non-existent
        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                "/api/actors/99999", HttpMethod.DELETE, null, Void.class);
        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        // CREATE with invalid data (if validation is present)
//        Actor invalid = new Actor();
//        ResponseEntity<String> invalidResponse = restTemplate.postForEntity("/api/actors", invalid, String.class);
//        assertThat(invalidResponse.getStatusCode()).isIn(HttpStatus.BAD_REQUEST, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}