package com.alex.cryptoBackend.service;

import com.alex.cryptoBackend.dto.WalletDto;

import java.util.List;

public interface WalletService {
    WalletDto createWallet(WalletDto wallet);
    WalletDto updateWallet(WalletDto wallet, Long id);
    WalletDto getWalletById(Long id);
    List<WalletDto> getWalletsByUser(Long id);
    List<WalletDto> getAllWallets();
    WalletDto getByUserAndCurrency(Long userId, String abbreviation);
    void delete(Long id);
}
