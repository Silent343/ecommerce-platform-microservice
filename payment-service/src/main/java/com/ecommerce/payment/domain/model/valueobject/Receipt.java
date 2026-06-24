package com.ecommerce.payment.domain.model.valueobject;

import java.time.Instant;
import java.util.Objects;

/**
 * Comprobante de pago. Value object inmutable que se genera al completar el
 * cobro: número de recibo y momento de emisión.
 */
public final class Receipt {

    private final String receiptNumber;
    private final Instant issuedAt;

    private Receipt(String receiptNumber, Instant issuedAt) {
        this.receiptNumber = receiptNumber;
        this.issuedAt = issuedAt;
    }

    public static Receipt issue(PaymentId paymentId) {
        // Número de recibo legible derivado del id del pago
        String number = "REC-" + paymentId.value().toString().substring(0, 8).toUpperCase();
        return new Receipt(number, Instant.now());
    }

    public static Receipt of(String receiptNumber, Instant issuedAt) {
        return new Receipt(receiptNumber, issuedAt);
    }

    public String receiptNumber() {
        return receiptNumber;
    }

    public Instant issuedAt() {
        return issuedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Receipt receipt)) return false;
        return Objects.equals(receiptNumber, receipt.receiptNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(receiptNumber);
    }
}
