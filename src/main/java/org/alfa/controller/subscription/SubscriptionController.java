package org.alfa.controller.subscription;



import org.alfa.service._dto.SubscriptionDto;
import org.alfa.service.auth.jwt.CustomUserDetails;
import org.springframework.http.ResponseEntity;

public interface SubscriptionController {


    ResponseEntity<SubscriptionDto> getMyActiveSubscription(CustomUserDetails userDetails);
}
