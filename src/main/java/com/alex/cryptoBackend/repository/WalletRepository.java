package com.alex.cryptoBackend.repository;

import com.alex.cryptoBackend.model.Currency;
import com.alex.cryptoBackend.model.User;
import com.alex.cryptoBackend.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {
    List<Wallet> findByUser(User user);
    List<Wallet> findByUserId(Long userId);
    @Query("SELECT a FROM Wallet a WHERE a.user.id = ?1 AND a.currency.abbreviation <> 'USDT'")
    List<Wallet> findByUserIdNoUSDT(Long userId);
    List<Wallet> findByCurrency(Currency currency);
    Optional<Wallet> findByUserAndCurrency(User user, Currency currency);
    @Query("SELECT a FROM Wallet a WHERE a.user = ?1 AND a.currency.abbreviation = 'USDT'")
    Optional<Wallet> findByUserUSDT(User user);
    Optional<Wallet> findWalletByUserIdAndCurrencyAbbreviation(Long userId, String currencyAbbreviation);
}
