package org.alfa.controller.payment;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.alfa.service._dto.CreatePaymentRequest;
import org.alfa.service._dto.PaymentDto;
import org.alfa.service.auth.jwt.CustomUserDetails;
import org.alfa.service.payment.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentControllerImpl implements PaymentController {

    private final PaymentService paymentService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping()
    @Override
    public ResponseEntity<PaymentDto> createPayment(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody CreatePaymentRequest tariffId
    ) {
        return ResponseEntity.ok(paymentService.create(userDetails.id(), tariffId.tariffId()));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{paymentId}")
    @Override
    public ResponseEntity<PaymentDto> getPayment(
            @PathVariable UUID paymentId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(paymentService.getByIdAndUserId(paymentId, userDetails.id()));
    }

    //todo заглушка bePaid
    @PostMapping("/webhook")
    @Override
    public ResponseEntity<Void> webhook(@RequestBody String payload) {
        paymentService.handleWebhook(payload);
        return ResponseEntity.ok().build();
    }
}

