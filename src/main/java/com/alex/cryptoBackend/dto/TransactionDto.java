package com.alex.cryptoBackend.dto;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionDto {
    private Long id;
    private Long userSenderId;
    private Long userReceiverId;
    private String currency;
    private Long walletSenderId;
    private Long walletReceiverId;
    private BigDecimal amount;
    private LocalDateTime time;
}
