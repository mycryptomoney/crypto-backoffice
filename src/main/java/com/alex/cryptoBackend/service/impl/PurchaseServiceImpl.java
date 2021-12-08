package com.alex.cryptoBackend.service.impl;

import com.alex.cryptoBackend.dto.PurchaseDto;
import com.alex.cryptoBackend.mapper.MapMapper;
import com.alex.cryptoBackend.model.Currency;
import com.alex.cryptoBackend.model.Purchase;
import com.alex.cryptoBackend.model.Wallet;
import com.alex.cryptoBackend.repository.CurrencyRepository;
import com.alex.cryptoBackend.repository.PurchaseRepository;
import com.alex.cryptoBackend.repository.WalletRepository;
import com.alex.cryptoBackend.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.alex.cryptoBackend.exception.code.AlertCode.*;

@Service
@RequiredArgsConstructor
public class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final CurrencyRepository currencyRepository;
    private final WalletRepository walletRepository;
    private final MapMapper mapper;

    @Override
    @Transactional
    public PurchaseDto executePurchase(Long walletId, BigDecimal amount) {
        Wallet walletReceiver = walletRepository.findById(walletId).orElseThrow(() -> new IllegalArgumentException(WALLET_NOT_EXISTS.name()));
        Wallet walletUsdt = walletRepository.findByUserUSDT(walletReceiver.getUser()).orElseThrow(() -> new IllegalArgumentException(WALLET_NOT_EXISTS.name()));
        Currency currency = walletReceiver.getCurrency();
        if (!currency.getActivated()) {
            throw new UnsupportedOperationException(CURRENCY_IS_NOT_ACTIVATED.name());
        }
        BigDecimal amountUsdt = amount.multiply(currency.getValue());
        if (walletUsdt.getAmount().compareTo(amountUsdt) < 0) {
            throw new IllegalArgumentException(NOT_ENOUGH_MONEY.name());
        }
        walletUsdt.setAmount(walletUsdt.getAmount().subtract(amountUsdt));
        walletReceiver.setAmount(walletReceiver.getAmount().add(amount));
        walletRepository.save(walletUsdt);
        walletRepository.save(walletReceiver);


        Purchase purchase = new Purchase();
        purchase.setAmount(amount);
        purchase.setPrice(amountUsdt.multiply(BigDecimal.valueOf(-1)));
        purchase.setWallet(walletReceiver);
        purchase.setTime(LocalDateTime.now());

        purchaseRepository.save(purchase);

        return mapper.toDto(purchase);
    }

    @Override
    @Transactional
    public PurchaseDto sellPurchase(Long walletId, BigDecimal amount) {
        Wallet wallet = walletRepository.findById(walletId).orElseThrow(() -> new IllegalArgumentException(WALLET_NOT_EXISTS.name()));
        Wallet walletUsdt = walletRepository.findByUserUSDT(wallet.getUser()).orElseThrow(() -> new IllegalArgumentException(WALLET_NOT_EXISTS.name()));
        Currency currency = wallet.getCurrency();
        if (!currency.getActivated()) {
            throw new UnsupportedOperationException(CURRENCY_IS_NOT_ACTIVATED.name());
        }
        BigDecimal amountUsdt = amount.multiply(currency.getValue());
        if (wallet.getAmount().compareTo(amount) < 0) {
            throw new IllegalArgumentException(NOT_ENOUGH_MONEY.name());
        }
        wallet.setAmount(wallet.getAmount().subtract(amount));
        walletUsdt.setAmount(walletUsdt.getAmount().add(amountUsdt));
        walletRepository.save(walletUsdt);
        walletRepository.save(wallet);

        Purchase purchase = new Purchase();
        purchase.setAmount(amount.multiply(BigDecimal.valueOf(-1)));
        purchase.setPrice(amountUsdt);
        purchase.setWallet(wallet);
        purchase.setTime(LocalDateTime.now());

        purchaseRepository.save(purchase);

        return mapper.toDto(purchase);
    }

    @Override
    public List<PurchaseDto> getAllPurchasesByWallet(Long walletId) {
        return mapper.toPurchaseDtoList(purchaseRepository.findAllByWalletId(walletId));
    }

    @Override
    public List<PurchaseDto> getAllPurchasesByPeriodByWallet(Long walletId, LocalDateTime start, LocalDateTime end) {
        return mapper.toPurchaseDtoList(purchaseRepository.findAllByWalletIdAndTimeBetween(walletId, start, end));
    }

    @Override
    public List<PurchaseDto> getAllPurchasesByUser(Long userId) {
        List<Wallet> wallets = walletRepository.findByUserId(userId);
        List<Purchase> purchases = new ArrayList<>();
        for (var wallet : wallets) {
            purchases.addAll(purchaseRepository.findAllByWallet(wallet));
        }
        return mapper.toPurchaseDtoList(purchases);
    }

    @Override
    public List<PurchaseDto> getAllPurchasesByPeriodByUser(Long userId, LocalDateTime start, LocalDateTime end) {
        List<Wallet> wallets = walletRepository.findByUserId(userId);
        List<Purchase> purchases = new ArrayList<>();
        for (var wallet : wallets) {
            purchases.addAll(purchaseRepository.findAllByWalletIdAndTimeBetween(wallet.getId(), start, end));
        }
        return mapper.toPurchaseDtoList(purchases);
    }
}
