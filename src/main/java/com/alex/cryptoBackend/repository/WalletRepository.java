package com.alex.cryptoBackend.repository;

import com.alex.cryptoBackend.model.Currency;
import com.alex.cryptoBackend.model.User;
import com.alex.cryptoBackend.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
    List<Wallet> findByUser(User user);
    List<Wallet> findByCurrency(Currency currency);
    Optional<Wallet> findByUserAndCurrency(User user, Currency currency);
    Optional<Wallet> findWalletByUserIdAndCurrencyAbbreviation(Long userId, String currencyAbbreviation);
}
