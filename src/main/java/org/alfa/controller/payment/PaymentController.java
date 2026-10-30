package org.alfa.controller.payment;


import org.alfa.service._dto.CreatePaymentRequest;
import org.alfa.service._dto.PaymentDto;
import org.alfa.service.auth.jwt.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


import java.util.UUID;

public interface PaymentController {

    @PostMapping()
    ResponseEntity<PaymentDto> createPayment(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody CreatePaymentRequest tariffId
    );

    @GetMapping("/{paymentId}")
    ResponseEntity<PaymentDto> getPayment(
            @PathVariable UUID paymentId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    );

    ResponseEntity<Void> webhook(String payload);
}
