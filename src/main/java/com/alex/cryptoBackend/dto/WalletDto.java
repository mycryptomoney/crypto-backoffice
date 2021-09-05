package com.alex.cryptoBackend.dto;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class WalletDto {
    private Long id;
    @NotNull(message = "Wallet can't be null")
    private Long userId;
    @NotNull(message = "Currency need to be selected")
    private CurrencyDto currency;
    private BigDecimal amount;
}
