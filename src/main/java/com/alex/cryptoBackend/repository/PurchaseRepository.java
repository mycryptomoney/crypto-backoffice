package com.alex.cryptoBackend.repository;

import com.alex.cryptoBackend.model.Purchase;
import com.alex.cryptoBackend.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    List<Purchase> findAllByWalletId(Long walletId);
    List<Purchase> findAllByWallet(Wallet wallet);
    List<Purchase> findAllByWalletIdAndTimeBetween(Long walletId, LocalDateTime start, LocalDateTime end);
}
