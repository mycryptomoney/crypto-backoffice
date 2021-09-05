package com.alex.cryptoBackend.repository;

import com.alex.cryptoBackend.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {
    Optional<Currency> findByName(String name);
    Optional<Currency> findByAbbreviation(String abbreviation);
}
