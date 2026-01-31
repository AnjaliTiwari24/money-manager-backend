package com.moneymanager.repository;

import com.moneymanager.model.Currency;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CurrencyRepository extends MongoRepository<Currency, String> {
    Optional<Currency> findByCode(String code);
}
