package org.alfa.controller.auth;


import org.alfa.service.auth.dto.AuthRequest;
import org.alfa.service.auth.dto.AuthResponse;
import org.springframework.http.ResponseEntity;

public interface AuthController {


    ResponseEntity<AuthResponse> register(AuthRequest authRequest);

    ResponseEntity<AuthResponse> login(AuthRequest registerRequest);


    ResponseEntity<String> validate(String authHeader);

    ResponseEntity<AuthResponse> refreshToken(String authHeader);
}
