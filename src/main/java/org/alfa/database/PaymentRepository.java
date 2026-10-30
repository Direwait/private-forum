package org.alfa.database;

import org.alfa.database.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    Optional<Payment> findByTrackingId(String trackingId);

    Optional<Payment> findByIdAndUserId(UUID paymentId, UUID userId);
}
