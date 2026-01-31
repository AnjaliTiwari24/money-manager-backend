package com.moneymanager.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "currencies")
public class Currency {
    @Id
    private String id;
    private String code; // "INR", "USD", "EUR", etc.
    private String symbol; // "₹", "$", "€", etc.
    private String name; // "Indian Rupee", "US Dollar", etc.
    private double exchangeRateToINR; // Exchange rate relative to INR

    public Currency() {
    }

    public Currency(String code, String symbol, String name, double exchangeRateToINR) {
        this.code = code;
        this.symbol = symbol;
        this.name = name;
        this.exchangeRateToINR = exchangeRateToINR;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getExchangeRateToINR() {
        return exchangeRateToINR;
    }

    public void setExchangeRateToINR(double exchangeRateToINR) {
        this.exchangeRateToINR = exchangeRateToINR;
    }
}
