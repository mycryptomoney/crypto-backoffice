package com.alex.cryptoBackend.controller;

import com.alex.cryptoBackend.dto.StartEndTime;
import com.alex.cryptoBackend.dto.TransactionDto;
import com.alex.cryptoBackend.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping()
    public ResponseEntity<List<TransactionDto>> allTransactions() {
        List<TransactionDto> transactions = transactionService.getAllTransactions();
        return new ResponseEntity<>( transactions, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/period")
    public ResponseEntity<List<TransactionDto>> findAllTransactionsByPeriod(@RequestBody StartEndTime startEndTime) {
        List<TransactionDto> transactions = transactionService.getAllTransactionsByPeriod(startEndTime.getStartDate(), startEndTime.getEndDate());
        return new ResponseEntity<>( transactions, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionDto> findTransactionById(@PathVariable Long id) {
        TransactionDto transaction = transactionService.getTransactionById(id);
        return new ResponseEntity<>( transaction, HttpStatus.OK);
    }

    @GetMapping("/wallets/{id}")
    public ResponseEntity<List<TransactionDto>> allWalletsTransactions(@PathVariable Long id) {
        List<TransactionDto> transactions = transactionService.getAllWalletsTransactions(id);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @GetMapping("/wallets/positive/{id}")
    public ResponseEntity<List<TransactionDto>> allPositiveWalletsTransactionsByPeriod(@PathVariable Long id, @RequestBody StartEndTime startEndTime) {
        List<TransactionDto> transactions = transactionService.getTransactionsByWalletPositivePeriod(id, startEndTime.getStartDate(), startEndTime.getEndDate());
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @GetMapping("/wallets/negative/{id}")
    public ResponseEntity<List<TransactionDto>> allNegativeWalletsTransactionsByPeriod(@PathVariable Long id, @RequestBody StartEndTime startEndTime) {
        List<TransactionDto> transactions = transactionService.getTransactionsByWalletNegativePeriod(id, startEndTime.getStartDate(), startEndTime.getEndDate());
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @PostMapping("/{senderId}/to/{receiverId}")
    public ResponseEntity<TransactionDto> executeTransaction(@PathVariable("senderId") Long senderId, @PathVariable("receiverId") Long receiverId, @RequestParam BigDecimal amount) {
        TransactionDto transactions = transactionService.executeTransactionWithWallets(senderId, receiverId, amount);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

}
