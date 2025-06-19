package com.example.pagila_api.model;

import jakarta.persistence.*;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "actor")
public class Actor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "actor_id")
    private Integer actorId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "last_update", nullable = false)
    private LocalDateTime lastUpdate;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "film_actor",
            joinColumns = @JoinColumn(name = "actor_id"),
            inverseJoinColumns =  @JoinColumn(name = "film_id")
    )
    private List<Film> films;

// Getters and setters

    public Integer getActorId() {
        return actorId;
    }

    public void setActorId(Integer actorId) {
        this.actorId = actorId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Film> getFilms() {
        return films;
    }

    public void setFilms(List<Film> films) {
        this.films = films;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    @PrePersist
    @PreUpdate
    public void setLastUpdate() {
        this.lastUpdate = LocalDateTime.now();
    }
}
