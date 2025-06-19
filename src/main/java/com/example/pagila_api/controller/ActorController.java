package com.example.pagila_api.controller;

import com.example.pagila_api.model.Actor;
import com.example.pagila_api.service.ActorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/actors")
public class ActorController {

    private final ActorService actorService;

    public ActorController(ActorService actorService) {
        this.actorService = actorService;
    }

    @GetMapping
    public List<Actor> getAllActors() {
        return actorService.getAllActors();
    }

    @GetMapping("/{id}")
    public Actor getActorById(@PathVariable int id) {
        return actorService.getActorById(id);
    }

    @PostMapping
    public ResponseEntity<Actor> addActor(@RequestBody Actor actor) {
        Actor createdActor = actorService.addActor(actor);
        return ResponseEntity.status(201).body(createdActor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Actor> updateActor(@PathVariable int id, @RequestBody Actor updatedActor) {
        Actor actor = actorService.updateActor(id, updatedActor);
        return ResponseEntity.ok(actor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Actor> deleteActorById(@PathVariable int id) {
        actorService.deleteActorById(id);
        return ResponseEntity.noContent().build();
    }
}
