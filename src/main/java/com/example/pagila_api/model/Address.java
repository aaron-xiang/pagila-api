package com.example.pagila_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Integer addressId;

    @NotNull(message = "Address is required")
    @Size(max = 50, message = "Address must be less than 50 characters")
    @Column(name = "address", nullable = false, length = 50)
    private String address;

    @Size(max = 50, message = "Address2 must be less than 50 characters")
    @Column(name = "address2", length = 50)
    private String address2;

    @NotNull(message = "District is required")
    @Size(max = 20, message = "District must be less than 20 characters")
    @Column(name = "district", nullable = false, length = 20)
    private String district;

    @NotNull(message = "City ID is required")
    @Column(name = "city_id", nullable = false)
    private Integer cityId;

    @Size(max = 10, message = "Postal code must be less than 10 characters")
    @Column(name = "postal_code", length = 10)
    private String postalCode;

    @NotNull(message = "Phone is required")
    @Size(max = 20, message = "Phone must be less than 20 characters")
    @Column(name = "phone", nullable = false, length = 20)
    private String phone;

    @Column(name = "last_update", nullable = false)
    private LocalDateTime lastUpdate;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", insertable = false, updatable = false)
    private City city;

    // Getters and setters
    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    @PrePersist
    @PreUpdate
    public void setLastUpdate() {
        this.lastUpdate = LocalDateTime.now();
    }
}