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

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

import static com.alex.cryptoBackend.exception.code.AlertCode.*;

@Service
@RequiredArgsConstructor
@Transactional
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final UserRepository userRepository;
    private final CurrencyRepository currencyRepository;
    private final MapMapper mapper;

    @Override
    public WalletDto createInitialWallet(Long userId) {
        final BigDecimal amount = new BigDecimal(0);
        final String abbreviation = "USD";
        Currency currency = currencyRepository.findByAbbreviation(abbreviation).orElseThrow(() -> new IllegalArgumentException(CURRENCY_NOT_EXISTS.name()));
        WalletDto walletDto = new WalletDto();
        walletDto.setUserId(userId);
        walletDto.setAmount(amount);
        Wallet wallet = mapper.toWallet(walletDto);
        wallet.setCurrency(currency);
        walletRepository.save(wallet);
        return mapper.toDto(wallet);
    }

    @Override
    public WalletDto createWallet(WalletDto wallet) {
        Wallet newWallet = mapper.toWallet(wallet);
        walletRepository.save(newWallet);
        return mapper.toDto(newWallet);
    }

    @Override
    public WalletDto updateWallet(WalletDto wallet, Long id) {
        walletRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(WALLET_NOT_EXISTS.name()));
        wallet.setId(id);
        Wallet updatedWallet = mapper.toWallet(wallet);
        walletRepository.save(updatedWallet);
        return mapper.toDto(updatedWallet);
    }

    @Override
    public WalletDto getWalletById(Long id) {
        Wallet wallet = walletRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(WALLET_NOT_EXISTS.name()));
        return mapper.toDto(wallet);
    }

    @Override
    public List<WalletDto> getWalletsByUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException(USER_NOT_EXISTS.name()));
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
        Wallet wallet = walletRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(WALLET_NOT_EXISTS.name()));
        if (wallet.getAmount().equals(BigDecimal.ZERO)) {
            walletRepository.delete(wallet);
        }
        else {
            throw new IllegalArgumentException(INVALID_WALLET_AMOUNT_TO_DELETE.name());
        }
    }

    @Override
    public WalletDto getByUserAndCurrency(Long userId, String abbreviation) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException(USER_NOT_EXISTS.name()));
        Currency currency = currencyRepository.findByAbbreviation(abbreviation).orElseThrow(() -> new IllegalArgumentException(CURRENCY_NOT_EXISTS.name()));
        WalletDto wallet = mapper.toDto(walletRepository.findByUserAndCurrency(user, currency).orElseThrow(() -> new IllegalArgumentException(WALLET_NOT_EXISTS.name())));
        return wallet;
    }
}
