package com.ecommerce.payment.interfaces.rest;

import com.ecommerce.payment.application.dto.command.ProcessPaymentCommand;
import com.ecommerce.payment.application.dto.command.RefundCommand;
import com.ecommerce.payment.application.dto.response.PaymentResponse;
import com.ecommerce.payment.application.dto.response.ReceiptResponse;
import com.ecommerce.payment.application.port.in.ProcessPaymentUseCase;
import com.ecommerce.payment.application.port.in.QueryPaymentUseCase;
import com.ecommerce.payment.application.port.in.RefundPaymentUseCase;
import com.ecommerce.payment.interfaces.rest.request.ProcessPaymentRequest;
import com.ecommerce.payment.interfaces.rest.request.RefundRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
@Tag(name = "Payments", description = "Procesamiento de pagos y reembolsos")
public class PaymentController {

    private final ProcessPaymentUseCase processPaymentUseCase;
    private final QueryPaymentUseCase queryPaymentUseCase;
    private final RefundPaymentUseCase refundPaymentUseCase;

    public PaymentController(ProcessPaymentUseCase processPaymentUseCase,
                            QueryPaymentUseCase queryPaymentUseCase,
                            RefundPaymentUseCase refundPaymentUseCase) {
        this.processPaymentUseCase = processPaymentUseCase;
        this.queryPaymentUseCase = queryPaymentUseCase;
        this.refundPaymentUseCase = refundPaymentUseCase;
    }

    @PostMapping("/process")
    @Operation(summary = "Procesa un pago manualmente")
    public ResponseEntity<PaymentResponse> process(@Valid @RequestBody ProcessPaymentRequest request) {
        ProcessPaymentCommand command = new ProcessPaymentCommand(
                request.orderId(), request.customerId(), request.amount(),
                request.currency(), request.method());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(processPaymentUseCase.process(command));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtiene un pago por su ID")
    public ResponseEntity<PaymentResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(queryPaymentUseCase.getById(id));
    }

    @GetMapping("/order/{orderId}")
    @Operation(summary = "Obtiene el pago asociado a un pedido")
    public ResponseEntity<PaymentResponse> getByOrder(@PathVariable UUID orderId) {
        return ResponseEntity.ok(queryPaymentUseCase.getByOrder(orderId));
    }

    @GetMapping("/{id}/receipt")
    @Operation(summary = "Obtiene el comprobante de un pago")
    public ResponseEntity<ReceiptResponse> getReceipt(@PathVariable UUID id) {
        return ResponseEntity.ok(queryPaymentUseCase.getReceipt(id));
    }

    @PostMapping("/{id}/refund")
    @Operation(summary = "Reembolsa un pago completado (requiere rol ADMIN)")
    public ResponseEntity<PaymentResponse> refund(
            @PathVariable UUID id,
            @Valid @RequestBody RefundRequest request) {
        return ResponseEntity.ok(refundPaymentUseCase.refund(new RefundCommand(id, request.reason())));
    }
}
