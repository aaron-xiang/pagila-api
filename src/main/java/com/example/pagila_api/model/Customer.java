package com.example.pagila_api.model;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Integer customerId;

    @Column(name = "first_name", nullable = false, length = 45)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 45)
    private String lastName;

    @Column(name = "email", length = 50)
    private String email;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(name = "active", nullable = false)
    private Boolean active;

    @Column(name = "create_date", nullable = false)
    private java.time.LocalDate createDate;

    @Column(name = "last_update")
    private java.time.LocalDateTime lastUpdate;

    // Relationships
    @OneToMany(mappedBy = "customer")
    private Set<Rental> rentals;

    @OneToMany(mappedBy = "customer")
    private Set<Payment> payments;

    // Getters and Setters
    // ...
}
