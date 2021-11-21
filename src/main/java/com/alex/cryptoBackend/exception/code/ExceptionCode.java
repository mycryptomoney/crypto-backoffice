package com.alex.cryptoBackend.exception.code;

public enum ExceptionCode {

    USER_EXCEPTION_MESSAGE("User doesn't exists"),
    WALLET_EXCEPTION_MESSAGE("Wallet doesn't exists"),
    MONEY_EXCEPTION("To delete wallet amount should be zero"),
    CURRENCY_EXCEPTION("Currency with this abbreviation doesn't exists"),
    NOT_ENOUGH_MONEY("User don't have this amount of money"),
    ROLE_EXCEPTION_MESSAGE("Role doesn't exist"),
    TRANSACTION_EXCEPTION_MESSAGE("Transaction doesn't exist");

    private final String message;

    ExceptionCode(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
