package com.example.pagila_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "rental")
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rental_id")
    private Integer rentalId;

    @NotNull(message = "Rental date is required")
    @Column(name = "rental_date", nullable = false)
    private LocalDateTime rentalDate;

    @NotNull(message = "Inventory ID is required")
    @Column(name = "inventory_id", nullable = false)
    private Integer inventoryId;

    @NotNull(message = "Customer ID is required")
    @Column(name = "customer_id", nullable = false)
    private Integer customerId;

    @Column(name = "return_date")
    private LocalDateTime returnDate;

    @NotNull(message = "Staff ID is required")
    @Column(name = "staff_id", nullable = false)
    private Integer staffId;

    @Column(name = "last_update", nullable = false)
    private LocalDateTime lastUpdate;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_id", insertable = false, updatable = false)
    private Inventory inventory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", insertable = false, updatable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staff_id", insertable = false, updatable = false)
    private Staff staff;

    @OneToMany(mappedBy = "rental", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Payment> payments;

    // Constructors
    public Rental() {}

    public Rental(Integer inventoryId, Integer customerId, Integer staffId) {
        this.inventoryId = inventoryId;
        this.customerId = customerId;
        this.staffId = staffId;
        this.rentalDate = LocalDateTime.now();
    }

    // Getters and setters
    public Integer getRentalId() {
        return rentalId;
    }

    public void setRentalId(Integer rentalId) {
        this.rentalId = rentalId;
    }

    public LocalDateTime getRentalDate() {
        return rentalDate;
    }

    public void setRentalDate(LocalDateTime rentalDate) {
        this.rentalDate = rentalDate;
    }

    public Integer getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(Integer inventoryId) {
        this.inventoryId = inventoryId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public LocalDateTime getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDateTime returnDate) {
        this.returnDate = returnDate;
    }

    public Integer getStaffId() {
        return staffId;
    }

    public void setStaffId(Integer staffId) {
        this.staffId = staffId;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    // Business logic methods
    public boolean isOverdue() {
        if (returnDate != null) {
            return false; // Already returned
        }

        // Assuming rental period is based on film's rental_duration
        // This would need to be calculated based on the film's rental duration
        LocalDateTime dueDate = rentalDate.plusDays(7); // Default 7 days, should be dynamic
        return LocalDateTime.now().isAfter(dueDate);
    }

    public boolean isActive() {
        return returnDate == null;
    }

    @PrePersist
    @PreUpdate
    public void setLastUpdate() {
        this.lastUpdate = LocalDateTime.now();
    }

    @PrePersist
    public void setRentalDate() {
        if (this.rentalDate == null) {
            this.rentalDate = LocalDateTime.now();
        }
    }

    @Override
    public String toString() {
        return "Rental{" +
                "rentalId=" + rentalId +
                ", rentalDate=" + rentalDate +
                ", inventoryId=" + inventoryId +
                ", customerId=" + customerId +
                ", returnDate=" + returnDate +
                ", staffId=" + staffId +
                ", lastUpdate=" + lastUpdate +
                '}';
    }
}