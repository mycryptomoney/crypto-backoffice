package com.alex.cryptoBackend.controller;

import com.alex.cryptoBackend.dto.WalletDto;
import com.alex.cryptoBackend.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wallets")
public class WalletController {

    private final WalletService walletService;

    @GetMapping
    public ResponseEntity<List<WalletDto>> getWallets() {
        List<WalletDto> wallets = walletService.getAllWallets();
        return new ResponseEntity<>(wallets, HttpStatus.OK);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<WalletDto>> getWalletsByUser(@PathVariable Long id) {
        List<WalletDto> wallets = walletService.getWalletsByUser(id);
        return new ResponseEntity<>(wallets, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WalletDto> getWalletById(@PathVariable Long id) {
        WalletDto wallet = walletService.getWalletById(id);
        return new ResponseEntity<>(wallet, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<WalletDto> createWallet(@Valid @RequestBody WalletDto wallet) {
        WalletDto newWallet = walletService.createWallet(wallet);
        return new ResponseEntity<>(newWallet, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WalletDto> updateWallet(@RequestBody WalletDto wallet, @PathVariable Long id) {
        WalletDto updatedWallet = walletService.updateWallet(wallet, id);
        return new ResponseEntity<>(updatedWallet, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWallet(@PathVariable Long id) {
        walletService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
