package com.alex.cryptoBackend.service.impl;

import com.alex.cryptoBackend.model.Purchase;
import com.alex.cryptoBackend.model.Wallet;
import com.alex.cryptoBackend.repository.PurchaseRepository;
import com.alex.cryptoBackend.repository.WalletRepository;
import com.alex.cryptoBackend.service.ProfitService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.alex.cryptoBackend.exception.code.AlertCode.WALLET_NOT_EXISTS;

@Service
@RequiredArgsConstructor
public class ProfitServiceImpl implements ProfitService {

    private final PurchaseRepository purchaseRepository;
    private final WalletRepository walletRepository;

    @Override
    public BigDecimal calculateProfitWallet(Long walletId) {
        Wallet wallet = walletRepository.findById(walletId).orElseThrow(() -> new IllegalArgumentException(WALLET_NOT_EXISTS.name()));
        return calculateProfit(wallet);
    }

    @Override
    public BigDecimal calculateProfitUser(Long userId) {
        List<Wallet> wallets = walletRepository.findByUserIdNoUSDT(userId);
        BigDecimal profit = wallets.stream().map(w -> calculateProfit(w)).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
        return profit;
    }

    private BigDecimal calculateProfit(Wallet wallet) {
        List<Purchase> purchases = purchaseRepository.findAllByWallet(wallet);
        BigDecimal moneyCalculations = purchases.stream().map(p -> p.getPrice()).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
        return calculateMoney(wallet).add(moneyCalculations);
    }

    @Override
    public BigDecimal calculateAllMoneyByWallet(Long walletId) {
        Wallet wallet = walletRepository.findById(walletId).orElseThrow(() -> new IllegalArgumentException(WALLET_NOT_EXISTS.name()));
        return calculateMoney(wallet);
    }

    @Override
    public BigDecimal calculateAllMoney(Long userId) {
        List<Wallet> wallets = walletRepository.findByUserId(userId);
        Optional<BigDecimal> allMoney = wallets.stream().map(w -> calculateMoney(w)).reduce(BigDecimal::add);
        return allMoney.orElse(BigDecimal.ZERO);
    }

    private static BigDecimal calculateMoney(Wallet wallet) {
        return wallet.getAmount().multiply(wallet.getCurrency().getValue());
    }
}
