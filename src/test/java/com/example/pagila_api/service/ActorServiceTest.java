package com.example.pagila_api.service;

import com.example.pagila_api.exception.ResourceNotFoundException;
import com.example.pagila_api.integration.BaseIntegrationTest;
import com.example.pagila_api.model.Actor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ActorServiceTest extends BaseIntegrationTest {

    @Autowired
    private ActorService actorService;

    @Test
    void testaddActor() {
        // CREATE
        Actor actor = new Actor();
        actor.setFirstName("Service");
        actor.setLastName("Test");

        Actor created = actorService.addActor(actor);

        assertThat(created.getActorId()).isNotNull();
        assertThat(created.getFirstName()).isEqualTo("Service");
        assertThat(created.getLastName()).isEqualTo("Test");
    }

    @Test
    void testGetActorById() {
        // CREATE first
        Actor actor = new Actor();
        actor.setFirstName("Get");
        actor.setLastName("Test");
        Actor created = actorService.addActor(actor);

        // READ
        Actor found = actorService.getActorById(created.getActorId());

        assertThat(found).isNotNull();
        assertThat(found.getActorId()).isEqualTo(created.getActorId());
        assertThat(found.getFirstName()).isEqualTo("Get");
        assertThat(found.getLastName()).isEqualTo("Test");
    }

    @Test
    void testGetAllActors() {
        // CREATE multiple
        Actor actor1 = new Actor();
        actor1.setFirstName("List");
        actor1.setLastName("One");
        actorService.addActor(actor1);

        Actor actor2 = new Actor();
        actor2.setFirstName("List");
        actor2.setLastName("Two");
        actorService.addActor(actor2);

        // READ all
        List<Actor> actors = actorService.getAllActors();

        assertThat(actors).hasSizeGreaterThanOrEqualTo(2);
    }

//    @Test
//    void testGetAllActorsWithPagination() {
//        // CREATE multiple
//        for (int i = 1; i <= 5; i++) {
//            Actor actor = new Actor();
//            actor.setFirstName("Page" + i);
//            actor.setLastName("Test" + i);
//            actorService.addActor(actor);
//        }
//
//        // READ with pagination
//        Page<Actor> page = actorService.getAllActors(PageRequest.of(0, 3));
//
//        assertThat(page.getContent()).hasSizeGreaterThanOrEqualTo(3);
//        assertThat(page.getTotalElements()).isGreaterThanOrEqualTo(5);
//    }

    @Test
    void testUpdateActor() {
        // CREATE
        Actor actor = new Actor();
        actor.setFirstName("Update");
        actor.setLastName("Original");
        Actor created = actorService.addActor(actor);

        // UPDATE
        Actor updateData = new Actor();
        updateData.setFirstName("Update");
        updateData.setLastName("Modified");

        Actor updated = actorService.updateActor(created.getActorId(), updateData);

        // VERIFY
        assertThat(updated.getActorId()).isEqualTo(created.getActorId());
        assertThat(updated.getFirstName()).isEqualTo("Update");
        assertThat(updated.getLastName()).isEqualTo("Modified");
    }

    @Test
    void testDeleteActor() {
        // CREATE
        Actor actor = new Actor();
        actor.setFirstName("Delete");
        actor.setLastName("Test");
        Actor created = actorService.addActor(actor);

        // DELETE
        actorService.deleteActorById(created.getActorId());

        // VERIFY deletion
        assertThatThrownBy(() -> actorService.getActorById(created.getActorId()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Actor not found with ID: " + created.getActorId());
    }

    @Test
    void testGetNonExistentActor() {
        // READ non-existent
        assertThatThrownBy(() -> actorService.getActorById(99999))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Actor not found with ID: 99999");
    }

    @Test
    void testUpdateNonExistentActor() {
        // UPDATE non-existent
        Actor updateData = new Actor();
        updateData.setFirstName("Non");
        updateData.setLastName("Existent");

        assertThatThrownBy(() -> actorService.updateActor(99999, updateData))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Actor not found with ID: 99999");
    }

    @Test
    void testDeleteNonExistentActor() {
        // DELETE non-existent
        assertThatThrownBy(() -> actorService.deleteActorById(99999))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Actor not found with ID: 99999");
    }
}