package com.ecommerce.payment.application.port.in;

import com.ecommerce.payment.application.dto.response.PaymentResponse;
import com.ecommerce.payment.application.dto.response.ReceiptResponse;

import java.util.UUID;

public interface QueryPaymentUseCase {
    PaymentResponse getById(UUID paymentId);
    PaymentResponse getByOrder(UUID orderId);
    ReceiptResponse getReceipt(UUID paymentId);
}
