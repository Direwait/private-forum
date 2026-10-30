package org.alfa.service.subscription;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.alfa.database.SubscriptionRepository;

import org.alfa.database.enums.PaymentStatus;
import org.alfa.database.enums.SubTier;
import org.alfa.database.model.Payment;
import org.alfa.database.model.Subscription;
import org.alfa.database.model.Tariff;
import org.alfa.database.model.User;
import org.alfa.service._dto.SubscriptionDto;
import org.alfa.service._mapper.SubscriptionMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionServiceImpl implements SubscriptionService {


    private final SubscriptionMapper subscriptionMapper;
    private final SubscriptionRepository subscriptionRepository;

    @Override
    public boolean hasAccess(UUID userId, SubTier required) {
        if (required == SubTier.FREE) {
            return true;                      // free tier: everyone (auth or not)
        }
        if (userId == null) {
            return false;                     // paid tier: need authenticated user
        }
        return subscriptionRepository
                .findByUserIdAndIsActiveTrueAndEndDateAfter(userId, LocalDateTime.now())
                .map(sub -> sub.getTariff().getTier().atLeast(required))
                .orElse(false);
    }

    @Scheduled(cron = "0 0 3 * * *")
    @Transactional
    @Override
    public void deactivateExpiredSubscriptions() {
        int updated = subscriptionRepository.deactivateExpired(LocalDateTime.now());
        if (updated > 0) {
            log.info("Deactivated {} expired subscriptions", updated);
        }
    }

    @Override
    public SubscriptionDto getMyActiveSubscription(UUID userId) {
        return subscriptionMapper.modelToDto(
                subscriptionRepository
                        .findByUserIdAndIsActiveTrueAndEndDateAfter(userId, LocalDateTime.now())
                        .orElseThrow(() -> new EntityNotFoundException("Active subscription not found"))
        );
    }

    @Override
    @Transactional
    public void activate(Payment payment) {
        if (payment.getStatus() != PaymentStatus.SUCCESS) {
            throw new IllegalStateException(
                    "Cannot activate subscription for non-success payment: " + payment.getId());
        }

        User user = payment.getUser();
        Tariff tariff = payment.getTariff();
        LocalDateTime now = LocalDateTime.now();

        Subscription subscription = subscriptionRepository
                .findByUserIdAndIsActiveTrueAndEndDateAfter(user.getId(), LocalDateTime.now())
                .orElseGet(() -> Subscription.builder()
                        .payment(payment)
                        .user(user)
                        .startDate(now)
                        .isActive(true)
                        .build());

        LocalDateTime base = subscription.getEndDate() != null
                && subscription.getEndDate().isAfter(now)
                ? subscription.getEndDate()
                : now;

        subscription.setTariff(tariff);
        subscription.setEndDate(base.plusDays(tariff.getDurationDays()));
        subscription.setActive(true);

        subscriptionRepository.save(subscription);

        log.info("Activated subscription for user {} until {}",
                user.getId(), subscription.getEndDate());
    }
}
