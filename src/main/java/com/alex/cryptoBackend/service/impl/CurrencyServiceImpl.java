package com.alex.cryptoBackend.service.impl;

import com.alex.cryptoBackend.dto.CurrencyDto;
import com.alex.cryptoBackend.mapper.MapMapper;
import com.alex.cryptoBackend.model.Currency;
import com.alex.cryptoBackend.repository.CurrencyRepository;
import com.alex.cryptoBackend.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.alex.cryptoBackend.exception.code.AlertCode.CURRENCY_IS_NOT_ACTIVATED;
import static com.alex.cryptoBackend.exception.code.AlertCode.CURRENCY_NOT_EXISTS;

@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyRepository currencyRepository;
    private final MapMapper mapper;
    @Override
    public List<CurrencyDto> getAllCurrencies() {
        List<CurrencyDto> currencies = mapper.toCurrencyListDto(currencyRepository.findAll());
        return currencies;
    }

    @Override
    public CurrencyDto getCurrencyById(Long id) {
        CurrencyDto currency = mapper.toDto(currencyRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(CURRENCY_NOT_EXISTS.name())));
        if (currency.getActivated()) {
            throw new IllegalArgumentException(CURRENCY_IS_NOT_ACTIVATED.name());
        }
        return currency;
    }

    @Override
    public CurrencyDto getCurrencyByName(String name) {
        CurrencyDto currency = mapper.toDto(currencyRepository.findByName(name).orElseThrow(() -> new IllegalArgumentException(CURRENCY_NOT_EXISTS.name())));
        return currency;
    }

    @Override
    public CurrencyDto createCurrency(CurrencyDto currencyDto) {
        Currency currency = mapper.toCurrency(currencyDto);
        currencyRepository.save(currency);
        return mapper.toDto(currency);
    }

    @Override
    public CurrencyDto updateCurrency(CurrencyDto currencyDto, Long id) {
        currencyRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(CURRENCY_NOT_EXISTS.name()));
        currencyDto.setId(id);
        Currency updatedCurrency = mapper.toCurrency(currencyDto);
        currencyRepository.save(updatedCurrency);
        return mapper.toDto(updatedCurrency);
    }
}
