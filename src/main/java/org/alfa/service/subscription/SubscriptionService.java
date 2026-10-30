package org.alfa.service.subscription;



import org.alfa.database.enums.SubTier;
import org.alfa.database.model.Payment;
import org.alfa.service._dto.SubscriptionDto;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface SubscriptionService {

    boolean hasAccess(UUID userId, SubTier required);

    @Scheduled(cron = "0 0 3 * * *") // каждый день в 3:00
    @Transactional
    void deactivateExpiredSubscriptions();

    SubscriptionDto getMyActiveSubscription(UUID userId);

    void activate(Payment payment);
}
