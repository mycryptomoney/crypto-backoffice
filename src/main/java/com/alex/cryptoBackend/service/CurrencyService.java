package com.alex.cryptoBackend.service;
import com.alex.cryptoBackend.dto.CurrencyDto;

import java.util.List;

public interface CurrencyService {
    List<CurrencyDto> getAllCurrencies();
    CurrencyDto getCurrencyById(Long id);
    CurrencyDto getCurrencyByName(String name);
    CurrencyDto createCurrency(CurrencyDto currencyDto);
    CurrencyDto updateCurrency(CurrencyDto currencyDto, Long id);
}
