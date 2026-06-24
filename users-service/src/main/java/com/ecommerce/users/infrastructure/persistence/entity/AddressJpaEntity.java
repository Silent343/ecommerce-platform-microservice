package com.ecommerce.users.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "addresses")
public class AddressJpaEntity {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    private String street;
    private String city;
    private String region;

    @Column(name = "postal_code")
    private String postalCode;

    private String country;

    @Column(name = "is_default")
    private boolean isDefault;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserJpaEntity user;

    protected AddressJpaEntity() {
    }

    public AddressJpaEntity(UUID id, String street, String city, String region,
                            String postalCode, String country, boolean isDefault,
                            UserJpaEntity user) {
        this.id = id;
        this.street = street;
        this.city = city;
        this.region = region;
        this.postalCode = postalCode;
        this.country = country;
        this.isDefault = isDefault;
        this.user = user;
    }

    public UUID getId() {
        return id;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getRegion() {
        return region;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCountry() {
        return country;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public UserJpaEntity getUser() {
        return user;
    }

    public void setUser(UserJpaEntity user) {
        this.user = user;
    }
}
