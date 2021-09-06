package com.alex.cryptoBackend.service;

import com.alex.cryptoBackend.dto.*;

public interface AuthService {
    JwtResponse authenticate(UserLoginDto loginRequest);
    RegisterResponse register(NewUserDto registerRequest);
    ConfirmTokenResponse confirmToken(String token);
}
