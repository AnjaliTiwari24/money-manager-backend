package com.moneymanager.service;

import com.moneymanager.model.Currency;
import com.moneymanager.repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CurrencyService {

    @Autowired
    private CurrencyRepository currencyRepository;

    public Currency createCurrency(Currency currency) {
        return currencyRepository.save(currency);
    }

    public Currency getCurrencyByCode(String code) {
        return currencyRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Currency not found"));
    }

    public Currency getCurrencyById(String id) {
        return currencyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Currency not found"));
    }

    public List<Currency> getAllCurrencies() {
        return currencyRepository.findAll();
    }

    public Currency updateCurrency(String id, Currency currency) {
        Currency existing = getCurrencyById(id);
        existing.setCode(currency.getCode());
        existing.setSymbol(currency.getSymbol());
        existing.setName(currency.getName());
        existing.setExchangeRateToINR(currency.getExchangeRateToINR());
        return currencyRepository.save(existing);
    }

    public void deleteCurrency(String id) {
        currencyRepository.deleteById(id);
    }

    public double convertCurrency(double amount, String fromCode, String toCode) {
        if (fromCode.equals(toCode)) {
            return amount;
        }

        Currency fromCurrency = getCurrencyByCode(fromCode);
        Currency toCurrency = getCurrencyByCode(toCode);

        // Convert to INR first, then to target currency
        double amountInINR = amount * fromCurrency.getExchangeRateToINR();
        return amountInINR / toCurrency.getExchangeRateToINR();
    }

    // Initialize default currencies
    public void initializeDefaultCurrencies() {
        if (currencyRepository.findByCode("INR").isEmpty()) {
            currencyRepository.save(new Currency("INR", "₹", "Indian Rupee", 1.0));
        }
        if (currencyRepository.findByCode("USD").isEmpty()) {
            currencyRepository.save(new Currency("USD", "$", "US Dollar", 83.5));
        }
        if (currencyRepository.findByCode("EUR").isEmpty()) {
            currencyRepository.save(new Currency("EUR", "€", "Euro", 91.2));
        }
        if (currencyRepository.findByCode("GBP").isEmpty()) {
            currencyRepository.save(new Currency("GBP", "£", "British Pound", 105.0));
        }
    }
}
