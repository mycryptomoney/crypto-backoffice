package com.alex.cryptoBackend.service.impl;

import com.alex.cryptoBackend.dto.CurrencyDto;
import com.alex.cryptoBackend.mapper.MapMapper;
import com.alex.cryptoBackend.model.Currency;
import com.alex.cryptoBackend.repository.CurrencyRepository;
import com.alex.cryptoBackend.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
        CurrencyDto currency = mapper.toDto(currencyRepository.findById(id).orElseThrow(IllegalArgumentException::new));
        return currency;
    }

    @Override
    public CurrencyDto getCurrencyByName(String name) {
        CurrencyDto currency = mapper.toDto(currencyRepository.findByName(name).orElseThrow(IllegalArgumentException::new));
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
        currencyRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        currencyDto.setId(id);
        Currency updatedCurrency = mapper.toCurrency(currencyDto);
        currencyRepository.save(updatedCurrency);
        return mapper.toDto(updatedCurrency);
    }
}
