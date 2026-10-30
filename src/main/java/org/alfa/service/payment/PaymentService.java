package org.alfa.service.payment;


import jakarta.validation.Payload;
import org.alfa.service._dto.PaymentDto;


import java.util.UUID;

public interface PaymentService {

    // создать платёж (PENDING) и получить ссылку на оплату от bePaid
    PaymentDto create(UUID user, UUID tariffId);

    //достать платеж для пуллинга
    PaymentDto getByIdAndUserId(UUID paymentId, UUID userId);

    // обновить платёж по вебхуку от bePaid (SUCCESS/FAILED)
    void handleWebhook(String payload);
}
