package com.ecommerce.notification.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "notification_preferences")
public class PreferenceJpaEntity {

    @Id
    @Column(name = "recipient_id", columnDefinition = "uuid")
    private UUID recipientId;

    /** Canales habilitados, separados por coma (ej: "EMAIL,IN_APP"). */
    @Column(name = "enabled_channels", nullable = false)
    private String enabledChannels;

    protected PreferenceJpaEntity() {
    }

    public PreferenceJpaEntity(UUID recipientId, String enabledChannels) {
        this.recipientId = recipientId;
        this.enabledChannels = enabledChannels;
    }

    public UUID getRecipientId() {
        return recipientId;
    }

    public String getEnabledChannels() {
        return enabledChannels;
    }
}
