package com.ecommerce.payment.application.port.in;

import com.ecommerce.payment.application.dto.command.RefundCommand;
import com.ecommerce.payment.application.dto.response.PaymentResponse;

public interface RefundPaymentUseCase {
    PaymentResponse refund(RefundCommand command);
}
