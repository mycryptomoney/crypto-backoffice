package com.alex.cryptoBackend.controller;

import com.alex.cryptoBackend.service.ProfitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/savings")
public class ProfitController {

    private final ProfitService profitService;

    @GetMapping("profit/{userId}")
    public ResponseEntity<BigDecimal> calculateProfitByUser(@PathVariable Long userId) {
        final BigDecimal profit = profitService.calculateProfitUser(userId);
        return new ResponseEntity<>(profit, HttpStatus.OK);
    }

    @GetMapping("profit/wallet/{walletId}")
    public ResponseEntity<BigDecimal> calculateProfitByWallet(@PathVariable Long walletId) {
        final BigDecimal profit = profitService.calculateProfitWallet(walletId);
        return new ResponseEntity<>(profit, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<BigDecimal> calculateMoneyByUser(@PathVariable Long userId) {
        final BigDecimal profit = profitService.calculateAllMoney(userId);
        return new ResponseEntity<>(profit, HttpStatus.OK);
    }

    @GetMapping("wallet/{walletId}")
    public ResponseEntity<BigDecimal> calculateMoneyByWallet(@PathVariable Long walletId) {
        final BigDecimal profit = profitService.calculateAllMoneyByWallet(walletId);
        return new ResponseEntity<>(profit, HttpStatus.OK);
    }


}
