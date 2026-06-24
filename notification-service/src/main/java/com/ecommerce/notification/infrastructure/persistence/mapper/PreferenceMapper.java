package com.ecommerce.notification.infrastructure.persistence.mapper;

import com.ecommerce.notification.domain.model.aggregate.NotificationPreference;
import com.ecommerce.notification.domain.model.valueobject.NotificationChannel;
import com.ecommerce.notification.domain.model.valueobject.RecipientId;
import com.ecommerce.notification.infrastructure.persistence.entity.PreferenceJpaEntity;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PreferenceMapper {

    public PreferenceJpaEntity toJpa(NotificationPreference p) {
        String csv = p.getEnabledChannels().stream()
                .map(NotificationChannel::name)
                .collect(Collectors.joining(","));
        return new PreferenceJpaEntity(p.getRecipientId().value(), csv);
    }

    public NotificationPreference toDomain(PreferenceJpaEntity e) {
        Set<NotificationChannel> channels = Arrays.stream(e.getEnabledChannels().split(","))
                .filter(s -> !s.isBlank())
                .map(NotificationChannel::valueOf)
                .collect(Collectors.toSet());
        return NotificationPreference.of(RecipientId.of(e.getRecipientId()), channels);
    }
}
