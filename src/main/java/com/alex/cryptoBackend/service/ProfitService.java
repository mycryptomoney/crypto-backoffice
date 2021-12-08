package com.alex.cryptoBackend.service;

import java.math.BigDecimal;

public interface ProfitService {
    BigDecimal calculateProfitWallet(Long walletId);

    BigDecimal calculateProfitUser(Long userId);

    BigDecimal calculateAllMoneyByWallet(Long walletId);

    BigDecimal calculateAllMoney(Long userId);
}
