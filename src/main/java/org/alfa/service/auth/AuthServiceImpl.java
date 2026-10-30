package org.alfa.service.auth;


import lombok.RequiredArgsConstructor;
import org.alfa.database.RefreshTokenRepository;
import org.alfa.database.UserRepository;
import org.alfa.database.enums.UserRole;
import org.alfa.database.model.RefreshToken;
import org.alfa.database.model.User;
import org.alfa.service.auth.dto.AuthRequest;
import org.alfa.service.auth.dto.AuthResponse;
import org.alfa.service.auth.jwt.CustomUserDetails;
import org.alfa.service.auth.jwt.JwtService;
import org.alfa.service.auth.util.RefreshTokenFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final RefreshTokenFactory refreshTokenFactory;

  private final UserRepository userRepository;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final PasswordEncoder passwordEncoder;
  private final RefreshTokenRepository refreshTokenRepository;



  //todo
  // изменить когда захочется сделать полноценный аналог бусти
  @Override
  @Transactional
  public AuthResponse register(AuthRequest authRequest) {
    if (userRepository.existsByName(authRequest.getUsername())) {
      throw new RuntimeException(authRequest.getUsername());
    }

    UserRole userRole = userRepository.count() == 0 ? UserRole.ADMIN : UserRole.USER;

    User savedUser = userRepository.save(
            User.builder()
                    .name(authRequest.getUsername())
                    .password(passwordEncoder.encode(authRequest.getPassword()))
                    .role(userRole)
                    .build()
    );

    UserDetails userDetails = new CustomUserDetails(
            savedUser.getId(),
            savedUser.getName(),
            savedUser.getPassword(),
            savedUser.getRole()
    );

    AuthResponse authResponse = jwtService.generateAuthToken(userDetails, savedUser.getId(), userRole.name());
    refreshTokenRepository.save(refreshTokenFactory.create(savedUser, authResponse.getRefreshToken()));

    return authResponse;
  }

  @Override
  @Transactional
  public AuthResponse login(AuthRequest authRequest) {
    Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    authRequest.getUsername(),
                    authRequest.getPassword()
            )
    );
    CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

    AuthResponse authResponse = jwtService.generateAuthToken(
            userDetails, userDetails.id(), userDetails.role().name()
    );

    User user = userRepository.getReferenceById(userDetails.id());
    refreshTokenRepository.save(refreshTokenFactory.create(user, authResponse.getRefreshToken()));
    return authResponse;
  }

  @Override
  public void validateToken(String token) {
    if (token == null || token.isBlank()) {
      throw new RuntimeException("Token is missing");
    }
    if (!jwtService.isTokenValid(token)) {
      throw new RuntimeException("Token is invalid or expired");
    }
    if (!jwtService.isAccessToken(token)) {
      throw new RuntimeException("Token is not an access token");
    }
  }

  @Override
  @Transactional
  public AuthResponse refreshToken(String token) {
    if (token == null || token.isBlank()) {
      throw new RuntimeException("Refresh token is missing");
    }
    if (!jwtService.isTokenValid(token)) {
      throw new RuntimeException("Refresh token is invalid or expired");
    }
    if (!jwtService.isRefreshToken(token)) {
      throw new RuntimeException("Provided token is not a refresh token");
    }

    RefreshToken stored = refreshTokenRepository.findByToken(token)
            .orElseThrow(() -> new RuntimeException("Refresh token not found"));

    if (stored.isRevoked()) {
      refreshTokenRepository.revokeAllByUserId(stored.getUser().getId());
      throw new RuntimeException("Refresh token has been revoked");
    }

    User user = stored.getUser();

    stored.setRevoked(true);
    refreshTokenRepository.save(stored);

    UserDetails userDetails = new CustomUserDetails(
            user.getId(),
            user.getName(),
            user.getPassword(),
            user.getRole()
    );

    AuthResponse authResponse = jwtService.generateAuthToken(
            userDetails, user.getId(), user.getRole().name()
    );

    refreshTokenRepository.save(
            refreshTokenFactory.create(user, authResponse.getRefreshToken())
    );

    return authResponse;
  }
}
