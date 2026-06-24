package com.ecommerce.users.domain.model.aggregate;

import com.ecommerce.users.domain.model.entity.Address;
import com.ecommerce.users.domain.model.event.UserRegisteredEvent;
import com.ecommerce.users.domain.model.valueobject.Email;
import com.ecommerce.users.domain.model.valueobject.FullName;
import com.ecommerce.users.domain.model.valueobject.Password;
import com.ecommerce.users.domain.model.valueobject.Role;
import com.ecommerce.users.domain.model.valueobject.UserId;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Agregado raíz del bounded context de usuarios.
 *
 * Reglas y responsabilidades:
 *  - Es la ÚNICA puerta de entrada para modificar usuarios y sus direcciones.
 *  - Protege sus invariantes (un solo address por defecto, email obligatorio, etc.).
 *  - Acumula eventos de dominio que la capa de aplicación publicará tras persistir.
 */
public class User {

    private final UserId id;
    private Email email;
    private Password password;
    private FullName fullName;
    private Role role;
    private boolean active;
    private final List<Address> addresses;
    private final Instant createdAt;
    private Instant updatedAt;

    private final transient List<Object> domainEvents = new ArrayList<>();

    private User(UserId id, Email email, Password password, FullName fullName,
                 Role role, boolean active, List<Address> addresses,
                 Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.role = role;
        this.active = active;
        this.addresses = addresses != null ? addresses : new ArrayList<>();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * Factory para registrar un usuario nuevo. Genera el evento UserRegisteredEvent.
     */
    public static User register(Email email, Password password, FullName fullName, Role role) {
        UserId newId = UserId.generate();
        Instant now = Instant.now();
        User user = new User(newId, email, password, fullName,
                role != null ? role : Role.CUSTOMER, true,
                new ArrayList<>(), now, now);
        user.registerEvent(new UserRegisteredEvent(
                newId.value(), email.value(), fullName.fullName(), now));
        return user;
    }

    /**
     * Reconstituye un usuario existente desde persistencia (sin disparar eventos).
     */
    public static User reconstitute(UserId id, Email email, Password password, FullName fullName,
                                    Role role, boolean active, List<Address> addresses,
                                    Instant createdAt, Instant updatedAt) {
        return new User(id, email, password, fullName, role, active, addresses, createdAt, updatedAt);
    }

    public void updateProfile(FullName newFullName) {
        this.fullName = newFullName;
        this.touch();
    }

    public void changePassword(Password newPassword) {
        this.password = newPassword;
        this.touch();
    }

    public void deactivate() {
        this.active = false;
        this.touch();
    }

    public void addAddress(Address address) {
        if (address.isDefault()) {
            addresses.forEach(Address::unmarkDefault);
        } else if (addresses.isEmpty()) {
            address.markAsDefault();
        }
        this.addresses.add(address);
        this.touch();
    }

    public void removeAddress(UUID addressId) {
        this.addresses.removeIf(a -> a.getAddressId().equals(addressId));
        this.touch();
    }

    private void touch() {
        this.updatedAt = Instant.now();
    }

    private void registerEvent(Object event) {
        this.domainEvents.add(event);
    }

    public List<Object> pullDomainEvents() {
        List<Object> copy = new ArrayList<>(domainEvents);
        domainEvents.clear();
        return copy;
    }

    public UserId getId() {
        return id;
    }

    public Email getEmail() {
        return email;
    }

    public Password getPassword() {
        return password;
    }

    public FullName getFullName() {
        return fullName;
    }

    public Role getRole() {
        return role;
    }

    public boolean isActive() {
        return active;
    }

    public List<Address> getAddresses() {
        return Collections.unmodifiableList(addresses);
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
