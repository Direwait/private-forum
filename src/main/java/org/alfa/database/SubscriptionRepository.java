package org.alfa.database;

import org.alfa.database.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {

    Optional<Subscription> findByUserIdAndIsActiveTrue(UUID userId);

    Optional<Subscription> findByUserIdAndIsActiveTrueAndEndDateAfter(UUID userId, LocalDateTime now);

    @Modifying
    @Query("UPDATE Subscription s SET s.isActive = false " +
            "WHERE s.isActive = true AND s.endDate < :now")
    int deactivateExpired(@Param("now") LocalDateTime now);
}
