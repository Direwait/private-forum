package org.alfa.service.payment;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.alfa.database.PaymentRepository;
import org.alfa.database.TariffRepository;
import org.alfa.database.UserRepository;
import org.alfa.database.enums.PaymentStatus;
import org.alfa.database.model.Payment;
import org.alfa.database.model.Tariff;
import org.alfa.database.model.User;
import org.alfa.service._dto.PaymentDto;
import org.alfa.service._mapper.PaymentMapper;
import org.alfa.service.subscription.SubscriptionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final ObjectMapper objectMapper;

    private final TariffRepository tariffRepository;
    private final SubscriptionService subscriptionService;

    private final UserRepository userRepository;


    @Override
    @Transactional
    public PaymentDto create(UUID userId, UUID tariffId) {

        Tariff tariff = tariffRepository.findById(tariffId)
                .orElseThrow(() -> new EntityNotFoundException("Tariff not found: " + tariffId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));

        Payment payment = Payment.builder()
                .user(user)
                .tariff(tariff)
                .trackingId(UUID.randomUUID().toString())
                .status(PaymentStatus.PENDING)
                .amount(tariff.getPrice())
                .currency(tariff.getCurrency())
                .description(tariff.getDescription())
                .build();

        paymentRepository.save(payment);
        return paymentMapper.modelToDto(payment);
    }

    @Transactional(readOnly = true)
    @Override
    public PaymentDto getByIdAndUserId(UUID paymentId, UUID userId) {
        return paymentMapper.modelToDto(
                paymentRepository.findByIdAndUserId(paymentId, userId)
                        .orElseThrow(() -> new EntityNotFoundException("Payment not found: " + paymentId)));
    }


    @Override
    @Transactional
    public void handleWebhook(String payload) {
        // === STUB ===
        // Real bePaid flow: verify signature, find Payment by gatewayId,
        // check idempotency (skip if already SUCCESS), update status/paidAt,
        // and call subscriptionService.activate(payment) only on SUCCESS.

        WebhookPayload data;
        try {
            data = objectMapper.readValue(payload, WebhookPayload.class);
        } catch (Exception e) {
            log.warn("Failed to parse webhook payload: {}", payload, e);
            return;
        }

        // Stub: lookup by trackingId (real bePaid: by gatewayId)
        Payment payment = paymentRepository.findByTrackingId(data.trackingId())
                .orElse(null);

        if (payment == null) {
            log.warn("Webhook for unknown trackingId: {}", data.trackingId());
            return; // 200 OK, do not trigger gateway retries
        }

        // Idempotency: already processed successfully
        if (payment.getStatus() == PaymentStatus.SUCCESS) {
            log.info("Payment {} already SUCCESS, skipping", payment.getId());
            return;
        }

        PaymentStatus newStatus = mapStatus(data.status());

        payment.setStatus(newStatus);
        payment.setGatewayId(data.gatewayId() != null
                ? data.gatewayId()
                : "stub-gateway-" + UUID.randomUUID());

        if (newStatus == PaymentStatus.SUCCESS) {
            payment.setPaidAt(OffsetDateTime.now().toLocalDateTime());
        }

        paymentRepository.save(payment);

        log.info("Webhook handled (STUB): payment {} marked as {}", payment.getId(), newStatus);

        if (newStatus == PaymentStatus.SUCCESS) {
            subscriptionService.activate(payment);
        }
    }

    private static PaymentStatus mapStatus(String status) {
        if (status == null) {
            return PaymentStatus.PENDING;
        }
        return switch (status.toLowerCase()) {
            case "successful", "SUCCESSFUL", "success", "paid" -> PaymentStatus.SUCCESS;
            case "failed", "declined", "canceled" -> PaymentStatus.FAILED;
            default -> PaymentStatus.PENDING;
        };
    }
}

record WebhookPayload (
        String trackingId,
        String status,
        String gatewayId
) {}