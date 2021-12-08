package com.alex.cryptoBackend.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PurchaseDto {
    private Long id;
    private Long walletId;
    private LocalDateTime time;
    private BigDecimal amount;
    private BigDecimal price;
}
