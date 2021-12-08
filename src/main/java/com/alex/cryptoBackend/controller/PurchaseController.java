package com.alex.cryptoBackend.controller;

import com.alex.cryptoBackend.dto.PurchaseDto;
import com.alex.cryptoBackend.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/purchase")
public class PurchaseController {

    private final PurchaseService purchaseService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<PurchaseDto>> showPurchasesByUser(@PathVariable Long userId) {
        final List<PurchaseDto> purchases = purchaseService.getAllPurchasesByUser(userId);
        return new ResponseEntity<>(purchases, HttpStatus.OK);
    }

    @GetMapping("/{userId}/period")
    public ResponseEntity<List<PurchaseDto>> showPurchasesByUserByPeriod(@PathVariable Long userId, @RequestParam final LocalDateTime start, @RequestParam final LocalDateTime end) {
        final List<PurchaseDto> purchases = purchaseService.getAllPurchasesByPeriodByUser(userId, start, end);
        return new ResponseEntity<>(purchases, HttpStatus.OK);
    }

    @GetMapping("wallet/{walletId}")
    public ResponseEntity<List<PurchaseDto>> showPurchasesByWallet(@PathVariable Long walletId) {
        final List<PurchaseDto> purchases = purchaseService.getAllPurchasesByWallet(walletId);
        return new ResponseEntity<>(purchases, HttpStatus.OK);
    }

    @GetMapping("wallet/{walletId}/period")
    public ResponseEntity<List<PurchaseDto>> showPurchasesByWalletByPeriod(@PathVariable Long walletId, @RequestParam final LocalDateTime start, @RequestParam final LocalDateTime end) {
        final List<PurchaseDto> purchases = purchaseService.getAllPurchasesByPeriodByWallet(walletId, start, end);
        return new ResponseEntity<>(purchases, HttpStatus.OK);
    }

    @PostMapping("wallet/{walletId}/buy")
    public ResponseEntity<PurchaseDto> executePurchasesByWallet(@PathVariable Long walletId, @RequestParam BigDecimal amount) {
        final PurchaseDto purchase = purchaseService.executePurchase(walletId, amount);
        return new ResponseEntity<>(purchase, HttpStatus.OK);
    }

    @PostMapping("wallet/{walletId}/sell")
    public ResponseEntity<PurchaseDto> sellCurrencyByWallet(@PathVariable Long walletId, @RequestParam BigDecimal amount) {
        final PurchaseDto purchase = purchaseService.sellPurchase(walletId, amount);
        return new ResponseEntity<>(purchase, HttpStatus.OK);
    }

}
