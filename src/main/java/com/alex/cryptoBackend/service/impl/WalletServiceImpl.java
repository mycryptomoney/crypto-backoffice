package com.alex.cryptoBackend.service.impl;

import com.alex.cryptoBackend.dto.UserDto;
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

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final UserRepository userRepository;
    private final CurrencyRepository currencyRepository;
    private final MapMapper mapper;

    private final static String USER_EXCEPTION_MESSAGE = "User doesn't exists";
    private final static String WALLET_EXCEPTION_MESSAGE = "Wallet doesn't exists";
    private final static String MONEY_EXCEPTION = "To delete wallet amount should be zero";
    private final static String CURRENCY_EXCEPTION = "Currency with this abbreviation doesn't exists";

    @Override
    public WalletDto createWallet(WalletDto wallet) {
        Wallet newWallet = mapper.toWallet(wallet);
        walletRepository.save(newWallet);
        return mapper.toDto(newWallet);
    }

    @Override
    public WalletDto updateWallet(WalletDto wallet, Long id) {
        walletRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(WALLET_EXCEPTION_MESSAGE));
        wallet.setId(id);
        Wallet updatedWallet = mapper.toWallet(wallet);
        walletRepository.save(updatedWallet);
        return mapper.toDto(updatedWallet);
    }

    @Override
    public WalletDto getWalletById(Long id) {
        Wallet wallet = walletRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(WALLET_EXCEPTION_MESSAGE));
        return mapper.toDto(wallet);
    }

    @Override
    public List<WalletDto> getWalletsByUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException(USER_EXCEPTION_MESSAGE));
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
        Wallet wallet = walletRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(WALLET_EXCEPTION_MESSAGE));
        if (wallet.getAmount().equals(BigDecimal.ZERO)) {
            walletRepository.delete(wallet);
        }
        else {
            throw new IllegalArgumentException(MONEY_EXCEPTION);
        }
    }

    @Override
    public WalletDto getByUserAndCurrency(Long userId, String abbreviation) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException(USER_EXCEPTION_MESSAGE));
        Currency currency = currencyRepository.findByAbbreviation(abbreviation).orElseThrow(() -> new IllegalArgumentException(CURRENCY_EXCEPTION));
        WalletDto wallet = mapper.toDto(walletRepository.findByUserAndCurrency(user, currency).orElseThrow(() -> new IllegalArgumentException(WALLET_EXCEPTION_MESSAGE)));
        return wallet;
    }
}
