package com.alex.cryptoBackend.controller;

import com.alex.cryptoBackend.dto.TransactionDto;
import com.alex.cryptoBackend.dto.UserDto;
import com.alex.cryptoBackend.dto.WalletDto;
import com.alex.cryptoBackend.service.TransactionService;
import com.alex.cryptoBackend.service.UserService;
import com.alex.cryptoBackend.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {
    private final UserService userService;
    private final TransactionService transactionService;
    private final WalletService walletService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllActiveUsers() {
        List<UserDto> users = userService.getAllActiveUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long userId) {
        UserDto user = userService.getUserById(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long userId, @RequestBody UserDto user) {
        UserDto updatedUser = userService.updateUser(user, userId);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @GetMapping("/{userId}/wallets")
    public ResponseEntity<List<WalletDto>> getUsersWallets(@PathVariable Long userId) {
        List<WalletDto> wallets = walletService.getWalletsByUser(userId);
        return new ResponseEntity<>(wallets, HttpStatus.OK);
    }

    @GetMapping("/{userId}/wallets/{walletAbbreviation}")
    public ResponseEntity<WalletDto> getUsersWalletById(@PathVariable String walletAbbreviation, @PathVariable Long userId) {
        WalletDto wallet = walletService.getByUserAndCurrency(userId, walletAbbreviation);
        return new ResponseEntity<>(wallet, HttpStatus.OK);
    }

    @PostMapping("/{userId}/wallets/{walletAbbreviation}/to/{receiverId}")
    public ResponseEntity<TransactionDto> getUsersWalletById(@RequestParam BigDecimal amount, @PathVariable String walletAbbreviation, @PathVariable Long userId, @PathVariable Long receiverId) {
        TransactionDto transaction = transactionService.executeTransactionUsersCurrency(userId, walletAbbreviation, receiverId, amount);
        return new ResponseEntity<>(transaction, HttpStatus.CREATED);
    }

    @GetMapping("/{userId}/wallets/{walletAbbreviation}/transactions")
    public ResponseEntity<List<TransactionDto>> getUsersWalletByIdTransactions(@PathVariable Long userId, @PathVariable String walletAbbreviation) {
        List<TransactionDto> transactions = transactionService.getAllWalletsTransactionsByUserAndCurrency(userId, walletAbbreviation);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }
}
