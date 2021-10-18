package com.alex.cryptoBackend.service;

import com.alex.cryptoBackend.model.ConfirmationToken;

public interface EmailService {
    void send (ConfirmationToken token);
}
