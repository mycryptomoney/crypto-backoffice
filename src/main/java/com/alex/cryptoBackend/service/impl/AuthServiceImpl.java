package com.alex.cryptoBackend.service.impl;

import com.alex.cryptoBackend.dto.*;
import com.alex.cryptoBackend.model.ConfirmationToken;
import com.alex.cryptoBackend.model.User;
import com.alex.cryptoBackend.security.jwt.JwtUtils;
import com.alex.cryptoBackend.service.AuthService;
import com.alex.cryptoBackend.service.ConfirmationTokenService;
import com.alex.cryptoBackend.service.UserService;
import com.alex.cryptoBackend.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Objects;

import static com.alex.cryptoBackend.exception.code.AlertCode.*;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final WalletService walletService;
    private final AuthenticationManager authenticationManager;
    private final ConfirmationTokenService confirmationTokenService;
    private final JwtUtils jwtUtils;

    @Override
    public JwtResponse authenticate(UserLoginDto loginRequest) {
        boolean isUserFrozen = userService.isUserFrozen(loginRequest.getUsername());
        if (isUserFrozen) {
            throw new IllegalArgumentException(FROZEN_USER.name());
        }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String jwt = jwtUtils.generateJwtToken(authentication);
        return new JwtResponse(jwt);
    }

    @Override
    public RegisterResponse register(NewUserDto registerRequest) {
        UserDto user = userService.createInitialUser(registerRequest);
        walletService.createInitialWallet(user.getId());
        return RegisterResponse.builder()
                .message(SUCCESSFUL_REGISTRATION.name())
                .username(user.getUsername())
                .build();
    }

    @Override
    @Transactional
    public ConfirmTokenResponse confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() -> new IllegalArgumentException(TOKEN_NOT_FOUND.name()));
        if (Objects.nonNull(confirmationToken.getConfirmedAt())) {
            throw new IllegalArgumentException(EMAIL_ALREADY_CONFIRMED.name());
        }
        final LocalDateTime expiredAt = confirmationToken.getExpiredAt();
        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException(TOKEN_EXPIRED.name());
        }
        final User user = confirmationToken.getUser();
        confirmationTokenService.setConfirmedAt(token);
        userService.enableUser(user.getEmail());
        return ConfirmTokenResponse.builder()
                .response(TOKEN_SUCCESSFULLY_CONFIRMED.name())
                .username(user.getUsername())
                .build();
    }
}
