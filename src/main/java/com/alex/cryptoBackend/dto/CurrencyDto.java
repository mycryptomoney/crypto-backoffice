package com.alex.cryptoBackend.dto;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Lob;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
public class CurrencyDto {
    private Long id;
    @NotNull(message = "Currency name can't be null")
    @NotBlank(message = "Currency name can't be blank")
    private String name;
    @Positive(message = "Value need to be positive")
    private BigDecimal value;
    @NotNull(message = "Currency abbreviation can't be null")
    @NotBlank(message = "Currency abbreviation can't be blank")
    private String abbreviation;
    @NotNull(message = "Currency name can't be null")
    private Boolean activated;
    private BigDecimal capitalisation;
    @Lob
    private Byte[] image;
}
