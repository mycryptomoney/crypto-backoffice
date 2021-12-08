package com.alex.cryptoBackend.exception.code;

public enum AlertCode {

    USER_NOT_EXISTS("User doesn't exists"),
    WALLET_NOT_EXISTS("Wallet doesn't exists"),
    INVALID_WALLET_AMOUNT_TO_DELETE("To delete wallet amount should be zero"),
    CURRENCY_NOT_EXISTS("Currency doesn't exists"),
    NOT_ENOUGH_MONEY("User don't have this amount of money"),
    ROLE_NOT_EXISTS("Role doesn't exist"),
    TRANSACTION_NOT_EXISTS("Transaction doesn't exist"),
    SUCCESSFUL_REGISTRATION("User have successfully registered!"),
    FROZEN_USER("You need to confirm your email!"),
    TOKEN_NOT_FOUND("Confirmation token not found"),
    EMAIL_ALREADY_CONFIRMED("Email is already confirmed"),
    TOKEN_EXPIRED("Confirmation token expired"),
    TOKEN_SUCCESSFULLY_CONFIRMED("Token is successfully confirmed"),
    USERNAME_ALREADY_EXISTS("Username already exists"),
    EMAIL_ALREADY_EXISTS("Email already exists"),
    CURRENCY_IS_NOT_ACTIVATED("Currency is not activated yet");

    private final String message;

    AlertCode(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}