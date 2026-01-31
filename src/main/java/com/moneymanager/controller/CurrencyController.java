package com.moneymanager.controller;

import com.moneymanager.model.Currency;
import com.moneymanager.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/currencies")
public class CurrencyController {

    @Autowired
    private CurrencyService currencyService;

    @PostMapping("/init")
    public ResponseEntity<String> initializeCurrencies() {
        try {
            currencyService.initializeDefaultCurrencies();
            return ResponseEntity.ok("Default currencies initialized");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to initialize currencies");
        }
    }

    @PostMapping
    public ResponseEntity<Currency> createCurrency(@RequestBody Currency currency) {
        try {
            Currency result = currencyService.createCurrency(currency);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<Currency>> getAllCurrencies() {
        try {
            List<Currency> currencies = currencyService.getAllCurrencies();
            return ResponseEntity.ok(currencies);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Currency> getCurrencyById(@PathVariable String id) {
        try {
            Currency currency = currencyService.getCurrencyById(id);
            return ResponseEntity.ok(currency);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<Currency> getCurrencyByCode(@PathVariable String code) {
        try {
            Currency currency = currencyService.getCurrencyByCode(code);
            return ResponseEntity.ok(currency);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Currency> updateCurrency(
            @PathVariable String id,
            @RequestBody Currency currency) {
        try {
            Currency result = currencyService.updateCurrency(id, currency);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCurrency(@PathVariable String id) {
        try {
            currencyService.deleteCurrency(id);
            return ResponseEntity.ok("Currency deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to delete currency");
        }
    }

    @PostMapping("/convert")
    public ResponseEntity<Map<String, Double>> convertCurrency(
            @RequestParam double amount,
            @RequestParam String fromCode,
            @RequestParam String toCode) {
        try {
            double convertedAmount = currencyService.convertCurrency(amount, fromCode, toCode);
            Map<String, Double> result = new HashMap<>();
            result.put("originalAmount", amount);
            result.put("convertedAmount", convertedAmount);
            result.put("from", (double) fromCode.hashCode());
            result.put("to", (double) toCode.hashCode());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
