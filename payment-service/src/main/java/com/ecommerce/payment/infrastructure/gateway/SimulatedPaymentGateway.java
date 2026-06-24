package com.ecommerce.payment.infrastructure.gateway;

import com.ecommerce.payment.application.port.out.PaymentGatewayPort;
import com.ecommerce.payment.domain.model.valueobject.Money;
import com.ecommerce.payment.domain.model.valueobject.PaymentMethod;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Implementación SIMULADA de la pasarela de pago. Aprueba el cobro siempre
 * (salvo casos triviales) y genera una referencia ficticia.
 *
 * En producción, esta clase se reemplazaría por una integración real con
 * Culqi, Stripe, Niubiz, etc., SIN tocar el dominio ni la aplicación: solo
 * se cambia este adaptador, que es justamente la ventaja de tener el puerto
 * PaymentGatewayPort.
 */
@Component
public class SimulatedPaymentGateway implements PaymentGatewayPort {

    @Override
    public ChargeResult charge(Money amount, PaymentMethod method) {
        // Simulación: un monto válido siempre se aprueba.
        // Aquí iría la llamada HTTP real a la pasarela.
        String reference = "SIM-" + UUID.randomUUID().toString().substring(0, 12).toUpperCase();
        return new ChargeResult(true, reference,
                "Cobro simulado aprobado vía " + method);
    }
}
