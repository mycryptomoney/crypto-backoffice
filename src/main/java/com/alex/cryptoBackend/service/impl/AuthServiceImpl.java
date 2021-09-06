package com.alex.cryptoBackend.service.impl;

import com.alex.cryptoBackend.dto.*;
import com.alex.cryptoBackend.model.ConfirmationToken;
import com.alex.cryptoBackend.model.User;
import com.alex.cryptoBackend.security.jwt.JwtUtils;
import com.alex.cryptoBackend.service.AuthService;
import com.alex.cryptoBackend.service.ConfirmationTokenService;
import com.alex.cryptoBackend.service.UserService;
import com.alex.cryptoBackend.service.WalletService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
@AllArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final WalletService walletService;
    private final AuthenticationManager authenticationManager;
    private final ConfirmationTokenService confirmationTokenService;
    private final JwtUtils jwtUtils;

    private final static String SUCCESSFUL_REGISTRATION = "User have successfully registered!";
    private final static String FROZEN_USER = "You need to confirm your email!";

    @Override
    public JwtResponse authenticate(UserLoginDto loginRequest) {
        boolean isUserFrozen = userService.isUserFrozen(loginRequest.getUsername());
        if (isUserFrozen) {
            throw new IllegalArgumentException(FROZEN_USER);
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
                .message(SUCCESSFUL_REGISTRATION)
                .username(user.getUsername())
                .build();
    }

    @Override
    @Transactional
    public ConfirmTokenResponse confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Token not found"));
        if (Objects.nonNull(confirmationToken.getConfirmedAt())) {
            throw new IllegalArgumentException("Email already confirmed");
        }
        final LocalDateTime expiredAt = confirmationToken.getExpiredAt();
        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Token expired");
        }
        final User user = confirmationToken.getUser();
        confirmationTokenService.setConfirmedAt(token);
        userService.enableUser(user.getEmail());
        return ConfirmTokenResponse.builder()
                .response("Successful confirmed")
                .username(user.getUsername())
                .build();
    }
}
