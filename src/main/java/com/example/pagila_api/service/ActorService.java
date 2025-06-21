package com.example.pagila_api.service;

import com.example.pagila_api.model.Actor;
import com.example.pagila_api.repository.ActorRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActorService {

    private final ActorRepository actorRepository;

    public ActorService(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    public List<Actor> getAllActors() {
        return actorRepository.findAll();
    }

    public Actor getActorById(int id) {
        return actorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Actor not found with ID: " + id));
    }

    public Actor addActor(Actor actor) {
        return actorRepository.save(actor);
    }

    public Actor updateActor(int id, Actor updatedActor) {
        Actor existingActor = actorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Actor not found with ID: " + id));

        existingActor.setFirstName(updatedActor.getFirstName());
        existingActor.setLastName(updatedActor.getLastName());

        return actorRepository.save(existingActor);
    }

    @Transactional
    public void deleteActorById(int id) {
        if (!actorRepository.existsById(id)) {
            throw new RuntimeException("Actor not found with ID: " + id);
        }
        Actor actor = actorRepository.findById(id).get(); // managed entity
        actorRepository.deleteById(id);
    }

}
