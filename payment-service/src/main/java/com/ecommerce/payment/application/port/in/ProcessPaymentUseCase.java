package com.ecommerce.payment.application.port.in;

import com.ecommerce.payment.application.dto.command.ProcessPaymentCommand;
import com.ecommerce.payment.application.dto.response.PaymentResponse;

public interface ProcessPaymentUseCase {
    PaymentResponse process(ProcessPaymentCommand command);
}
