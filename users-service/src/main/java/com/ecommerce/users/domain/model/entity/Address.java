package com.ecommerce.users.domain.model.entity;

import java.util.Objects;
import java.util.UUID;

/**
 * Entidad Address. Vive dentro del agregado User (no se accede directamente
 * desde fuera del agregado). Tiene identidad propia (addressId) pero su ciclo
 * de vida está gobernado por el agregado raíz User.
 */
public class Address {

    private final UUID addressId;
    private String street;
    private String city;
    private String region;
    private String postalCode;
    private String country;
    private boolean isDefault;

    public Address(UUID addressId, String street, String city, String region,
                   String postalCode, String country, boolean isDefault) {
        if (street == null || street.isBlank()) {
            throw new IllegalArgumentException("La calle no puede estar vacía");
        }
        if (city == null || city.isBlank()) {
            throw new IllegalArgumentException("La ciudad no puede estar vacía");
        }
        this.addressId = addressId != null ? addressId : UUID.randomUUID();
        this.street = street;
        this.city = city;
        this.region = region;
        this.postalCode = postalCode;
        this.country = country;
        this.isDefault = isDefault;
    }

    public static Address create(String street, String city, String region,
                                 String postalCode, String country, boolean isDefault) {
        return new Address(UUID.randomUUID(), street, city, region, postalCode, country, isDefault);
    }

    public void markAsDefault() {
        this.isDefault = true;
    }

    public void unmarkDefault() {
        this.isDefault = false;
    }

    public UUID getAddressId() {
        return addressId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Address address)) return false;
        return addressId.equals(address.addressId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(addressId);
    }
}
