package com.alex.cryptoBackend.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
public class CurrencyDto {
    private Long id;
    @NotNull(message = "Currency name can't be null")
    @NotBlank(message = "Currency name can't be blank")
    private String name;
    @Positive(message = "Value need to be positive")
    private Float value;
    @NotNull(message = "Currency abbreviation can't be null")
    @NotBlank(message = "Currency abbreviation can't be blank")
    private String abbreviation;
}
