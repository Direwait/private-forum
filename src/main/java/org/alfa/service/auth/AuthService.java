package org.alfa.service.auth;


import org.alfa.service.auth.dto.AuthRequest;
import org.alfa.service.auth.dto.AuthResponse;

public interface AuthService {

    AuthResponse register(AuthRequest authRequest);

    AuthResponse login(AuthRequest authRequest);

    void validateToken(String token);

    AuthResponse refreshToken(String token);

}
