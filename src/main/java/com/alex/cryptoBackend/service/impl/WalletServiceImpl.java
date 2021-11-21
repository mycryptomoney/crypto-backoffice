package com.alex.cryptoBackend.service.impl;

import com.alex.cryptoBackend.dto.WalletDto;
import com.alex.cryptoBackend.mapper.MapMapper;
import com.alex.cryptoBackend.model.Currency;
import com.alex.cryptoBackend.model.User;
import com.alex.cryptoBackend.model.Wallet;
import com.alex.cryptoBackend.repository.CurrencyRepository;
import com.alex.cryptoBackend.repository.UserRepository;
import com.alex.cryptoBackend.repository.WalletRepository;
import com.alex.cryptoBackend.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import static com.alex.cryptoBackend.exception.code.ExceptionCode.*;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final UserRepository userRepository;
    private final CurrencyRepository currencyRepository;
    private final MapMapper mapper;


    @Override
    public WalletDto createWallet(WalletDto wallet) {
        Wallet newWallet = mapper.toWallet(wallet);
        walletRepository.save(newWallet);
        return mapper.toDto(newWallet);
    }

    @Override
    public WalletDto updateWallet(WalletDto wallet, Long id) {
        walletRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(WALLET_EXCEPTION_MESSAGE.name()));
        wallet.setId(id);
        Wallet updatedWallet = mapper.toWallet(wallet);
        walletRepository.save(updatedWallet);
        return mapper.toDto(updatedWallet);
    }

    @Override
    public WalletDto getWalletById(Long id) {
        Wallet wallet = walletRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(WALLET_EXCEPTION_MESSAGE.name()));
        return mapper.toDto(wallet);
    }

    @Override
    public List<WalletDto> getWalletsByUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException(USER_EXCEPTION_MESSAGE.name()));
        List<Wallet> wallets = walletRepository.findByUser(user);
        return mapper.toWalletListDto(wallets);
    }

    @Override
    public List<WalletDto> getAllWallets() {
        List<Wallet> wallets = walletRepository.findAll();
        return mapper.toWalletListDto(wallets);
    }

    @Override
    public void delete(Long id) {
        Wallet wallet = walletRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(WALLET_EXCEPTION_MESSAGE.name()));
        if (wallet.getAmount().equals(BigDecimal.ZERO)) {
            walletRepository.delete(wallet);
        } else {
            throw new IllegalArgumentException(MONEY_EXCEPTION.name());
        }
    }

    @Override
    public WalletDto getByUserAndCurrency(Long userId, String abbreviation) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException(USER_EXCEPTION_MESSAGE.name()));
        Currency currency = currencyRepository.findByAbbreviation(abbreviation).orElseThrow(() -> new IllegalArgumentException(CURRENCY_EXCEPTION.name()));
        return mapper.toDto(walletRepository.findByUserAndCurrency(user, currency).orElseThrow(() -> new IllegalArgumentException(WALLET_EXCEPTION_MESSAGE.name())));
    }
}
