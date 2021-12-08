package com.alex.cryptoBackend.service;

import com.alex.cryptoBackend.dto.PurchaseDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface PurchaseService {
    PurchaseDto executePurchase(Long walletId, BigDecimal amount);
    PurchaseDto sellPurchase(Long walletId, BigDecimal amount);
    List<PurchaseDto> getAllPurchasesByWallet(Long walletId);
    List<PurchaseDto> getAllPurchasesByPeriodByWallet(Long walletId, LocalDateTime start, LocalDateTime end);
    List<PurchaseDto> getAllPurchasesByUser(Long userId);
    List<PurchaseDto> getAllPurchasesByPeriodByUser(Long userId, LocalDateTime start, LocalDateTime end);
}
