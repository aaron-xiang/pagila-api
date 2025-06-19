package com.example.pagila_api.repository;

import com.example.pagila_api.model.Actor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ActorRepository extends JpaRepository<Actor, Integer> {
    @Query("select a from Actor a join fetch a.films where actorId = :actorId")
    List<Actor> getActorByIdWithFilms(@Param("actorId") Integer actorId);
}
