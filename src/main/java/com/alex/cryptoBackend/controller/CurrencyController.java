package com.alex.cryptoBackend.controller;

import com.alex.cryptoBackend.dto.CurrencyDto;
import com.alex.cryptoBackend.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/currencies")
public class CurrencyController {

    private final CurrencyService currencyService;

    @GetMapping
    public ResponseEntity<List<CurrencyDto>> getCurrencies() {
        List<CurrencyDto> currencies = currencyService.getAllCurrencies();
        return new ResponseEntity<>(currencies, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CurrencyDto> getCurrencyById(@PathVariable Long id) {
        CurrencyDto currency = currencyService.getCurrencyById(id);
        return new ResponseEntity<>(currency, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CurrencyDto> createCurrency(@Valid @RequestBody CurrencyDto currencyDto) {
        CurrencyDto newCurrency = currencyService.createCurrency(currencyDto);
        return new ResponseEntity<>(newCurrency, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CurrencyDto> updateCurrency(@PathVariable Long id, @RequestBody CurrencyDto currencyDto) {
        CurrencyDto updatedCurrency = currencyService.updateCurrency(currencyDto, id);
        return new ResponseEntity<>(updatedCurrency, HttpStatus.OK);
    }
}
