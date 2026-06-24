package com.ecommerce.users.application.dto.response;

import com.ecommerce.users.domain.model.entity.Address;

import java.util.UUID;

public record AddressResponse(
        UUID addressId,
        String street,
        String city,
        String region,
        String postalCode,
        String country,
        boolean isDefault
) {
    public static AddressResponse from(Address a) {
        return new AddressResponse(
                a.getAddressId(), a.getStreet(), a.getCity(), a.getRegion(),
                a.getPostalCode(), a.getCountry(), a.isDefault());
    }
}
