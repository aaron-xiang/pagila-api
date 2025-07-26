package com.example.pagila_api.repository;

import com.example.pagila_api.integration.BaseIntegrationTest;
import com.example.pagila_api.model.Actor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class ActorRepositoryTest extends BaseIntegrationTest {

    @Autowired
    private ActorRepository actorRepository;

    @Test
    void testSaveAndFindActor() {
        Actor actor = new Actor();
        actor.setFirstName("John");
        actor.setLastName("Doe");
        Actor saved = actorRepository.save(actor);

        assertThat(saved.getActorId()).isNotNull();

        Actor found = actorRepository.findById(saved.getActorId()).orElse(null);
        assertThat(found).isNotNull();
        assertThat(found.getFirstName()).isEqualTo("John");
        assertThat(found.getLastName()).isEqualTo("Doe");
    }

    @Test
    void testFindAll() {
        List<Actor> actors = actorRepository.findAll();
        assertThat(actors).isNotNull();
    }

    @Test
    void testUpdateActor() {
        // CREATE
        Actor actor = new Actor();
        actor.setFirstName("Original");
        actor.setLastName("Name");
        Actor saved = actorRepository.save(actor);

        // UPDATE
        saved.setFirstName("Updated");
        saved.setLastName("Name");
        Actor updated = actorRepository.save(saved);

        // VERIFY
        assertThat(updated.getActorId()).isEqualTo(saved.getActorId());
        assertThat(updated.getFirstName()).isEqualTo("Updated");
        assertThat(updated.getLastName()).isEqualTo("Name");
    }

    @Test
    void testDeleteActor() {
        // CREATE
        Actor actor = new Actor();
        actor.setFirstName("ToDelete");
        actor.setLastName("Actor");
        Actor saved = actorRepository.save(actor);

        // DELETE
        actorRepository.deleteById(saved.getActorId());

        // VERIFY deletion
        Optional<Actor> deleted = actorRepository.findById(saved.getActorId());
        assertThat(deleted).isEmpty();
    }

    @Test
    void testFindNonExistentActor() {
        // READ non-existent
        Optional<Actor> notFound = actorRepository.findById(99999);

        assertThat(notFound).isEmpty();
    }
}