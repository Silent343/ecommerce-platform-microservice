package com.ecommerce.payment.application.port.out;

import com.ecommerce.payment.domain.model.valueobject.Money;
import com.ecommerce.payment.domain.model.valueobject.PaymentMethod;

/**
 * Puerto de salida hacia la pasarela de pago externa (Culqi, Stripe, etc.).
 * Hoy lo implementa un simulador; en producción se reemplaza por la integración
 * real sin tocar el dominio ni la aplicación.
 */
public interface PaymentGatewayPort {

    record ChargeResult(boolean approved, String gatewayReference, String message) {
    }

    ChargeResult charge(Money amount, PaymentMethod method);
}
