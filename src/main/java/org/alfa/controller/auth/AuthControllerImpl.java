package org.alfa.controller.auth;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.alfa.service.auth.dto.AuthRequest;
import org.alfa.service.auth.dto.AuthResponse;
import org.alfa.service.auth.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class AuthControllerImpl implements AuthController {

    private final AuthService authService;


    @PostMapping("/register")
    @Override
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody AuthRequest authRequest){
        AuthResponse jwtResponseDto = authService.register(authRequest);
        return ResponseEntity.ok(jwtResponseDto);
    }

    @PostMapping("/login")
    @Override
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest registerRequest) {
        AuthResponse jwtResponseDto = authService.login(registerRequest);
        return ResponseEntity.ok(jwtResponseDto);
    }

    @PostMapping("/validate")
    @Override
    public ResponseEntity<String> validate(@RequestHeader("Authorization") String authHeader) {
        authService.validateToken(authHeader);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestHeader("Authorization") String authHeader) {
        AuthResponse result = authService.refreshToken(authHeader);
        return ResponseEntity.ok(result);
    }
}