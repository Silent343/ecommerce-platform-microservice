package com.ecommerce.notification.application.service;

import com.ecommerce.notification.application.dto.command.UpdatePreferenceCommand;
import com.ecommerce.notification.application.dto.external.OrderConfirmedEvent;
import com.ecommerce.notification.application.dto.external.PaymentCompletedEvent;
import com.ecommerce.notification.application.dto.response.NotificationResponse;
import com.ecommerce.notification.application.dto.response.PreferenceResponse;
import com.ecommerce.notification.application.port.in.HandleEventUseCase;
import com.ecommerce.notification.application.port.in.ManagePreferenceUseCase;
import com.ecommerce.notification.application.port.in.QueryNotificationUseCase;
import com.ecommerce.notification.application.port.out.NotificationSenderPort;
import com.ecommerce.notification.domain.exception.NotificationNotFoundException;
import com.ecommerce.notification.domain.model.aggregate.Notification;
import com.ecommerce.notification.domain.model.aggregate.NotificationPreference;
import com.ecommerce.notification.domain.model.valueobject.NotificationChannel;
import com.ecommerce.notification.domain.model.valueobject.NotificationId;
import com.ecommerce.notification.domain.model.valueobject.NotificationType;
import com.ecommerce.notification.domain.model.valueobject.RecipientId;
import com.ecommerce.notification.domain.repository.NotificationRepository;
import com.ecommerce.notification.domain.repository.PreferenceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Orquesta la creación y envío de notificaciones. Al recibir un evento de otro
 * servicio, arma el mensaje según el tipo, respeta las preferencias del usuario
 * (canal), persiste la notificación y la "envía" vía el puerto de salida.
 */
@Service
public class NotificationService implements HandleEventUseCase, QueryNotificationUseCase, ManagePreferenceUseCase {

    private final NotificationRepository notificationRepository;
    private final PreferenceRepository preferenceRepository;
    private final NotificationSenderPort sender;

    public NotificationService(NotificationRepository notificationRepository,
                               PreferenceRepository preferenceRepository,
                               NotificationSenderPort sender) {
        this.notificationRepository = notificationRepository;
        this.preferenceRepository = preferenceRepository;
        this.sender = sender;
    }

    @Override
    @Transactional
    public void onOrderConfirmed(OrderConfirmedEvent event) {
        RecipientId recipient = RecipientId.of(event.customerId());
        String title = "¡Pedido confirmado!";
        String message = "Tu pedido " + shortId(event.orderId())
                + " fue confirmado y está siendo preparado. ¡Gracias por tu compra!";
        dispatch(recipient, NotificationType.ORDER_CONFIRMED, title, message);
    }

    @Override
    @Transactional
    public void onPaymentResult(PaymentCompletedEvent event) {
        RecipientId recipient = recipientFromOrder(event.orderId());
        if ("FAILED".equalsIgnoreCase(event.status())) {
            String title = "Problema con tu pago";
            String message = "No pudimos procesar el pago de tu pedido " + shortId(event.orderId())
                    + ". Por favor intenta nuevamente o usa otro método de pago.";
            dispatch(recipient, NotificationType.PAYMENT_FAILED, title, message);
        }
        // Si COMPLETED, el aviso al cliente lo cubre order.confirmed, así no duplicamos.
    }

    private void dispatch(RecipientId recipient, NotificationType type, String title, String message) {
        NotificationPreference pref = preferenceRepository.findByRecipient(recipient)
                .orElseGet(() -> NotificationPreference.defaultsFor(recipient));
        NotificationChannel channel = pref.preferredChannel();

        Notification notification = Notification.create(recipient, type, channel, title, message);
        Notification saved = notificationRepository.save(notification);
        sender.send(saved);
    }

    /**
     * El evento de pago no trae el customerId, solo el orderId. Como aún no
     * consultamos a orders-service por REST, derivamos el destinatario a partir
     * de notificaciones previas del mismo pedido si existieran; de lo contrario
     * se usa el propio orderId como referencia. (Punto de mejora: enriquecer el
     * evento de pago con customerId, o consultar orders vía REST.)
     */
    private RecipientId recipientFromOrder(UUID orderId) {
        return RecipientId.of(orderId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponse> getForRecipient(UUID recipientId) {
        return notificationRepository.findByRecipient(RecipientId.of(recipientId)).stream()
                .map(NotificationResponse::from)
                .toList();
    }

    @Override
    @Transactional
    public NotificationResponse markAsRead(UUID notificationId) {
        Notification notification = notificationRepository.findById(NotificationId.of(notificationId))
                .orElseThrow(() -> new NotificationNotFoundException(notificationId.toString()));
        notification.markAsRead();
        return NotificationResponse.from(notificationRepository.save(notification));
    }

    @Override
    @Transactional(readOnly = true)
    public PreferenceResponse getPreferences(UUID recipientId) {
        RecipientId recipient = RecipientId.of(recipientId);
        NotificationPreference pref = preferenceRepository.findByRecipient(recipient)
                .orElseGet(() -> NotificationPreference.defaultsFor(recipient));
        return PreferenceResponse.from(pref);
    }

    @Override
    @Transactional
    public PreferenceResponse update(UpdatePreferenceCommand command) {
        Set<NotificationChannel> channels = command.channels().stream()
                .map(NotificationChannel::valueOf)
                .collect(Collectors.toSet());
        NotificationPreference pref = NotificationPreference.of(
                RecipientId.of(command.recipientId()), channels);
        return PreferenceResponse.from(preferenceRepository.save(pref));
    }

    private String shortId(UUID id) {
        return "#" + id.toString().substring(0, 8).toUpperCase();
    }
}
