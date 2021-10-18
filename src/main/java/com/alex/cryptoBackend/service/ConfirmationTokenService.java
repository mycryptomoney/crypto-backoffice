package com.alex.cryptoBackend.service;

import com.alex.cryptoBackend.model.ConfirmationToken;

import java.util.Optional;

public interface ConfirmationTokenService {
    void saveConfirmationToken(ConfirmationToken confirmationToken);
    Optional<ConfirmationToken> getToken(String token);
    void setConfirmedAt(String token);
}
