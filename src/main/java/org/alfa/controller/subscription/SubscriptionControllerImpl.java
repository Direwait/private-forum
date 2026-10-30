package org.alfa.controller.subscription;

import lombok.RequiredArgsConstructor;
import org.alfa.service._dto.SubscriptionDto;
import org.alfa.service.auth.jwt.CustomUserDetails;
import org.alfa.service.subscription.SubscriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/subs")
public class SubscriptionControllerImpl implements SubscriptionController {

    private final SubscriptionService subscriptionService;

    @GetMapping("/active")
    @Override
    public ResponseEntity<SubscriptionDto> getMyActiveSubscription(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(subscriptionService.getMyActiveSubscription(userDetails.id()));
    }

}
