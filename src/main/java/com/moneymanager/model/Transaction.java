package com.moneymanager.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;

@Document(collection = "transactions")
public class Transaction {
    @Id
    private String id;
    private String type; // "INCOME" or "EXPENSE"
    private double amount;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime dateTime;
    private String description;
    private String category;
    private String division; // "OFFICE" or "PERSONAL"

    private String userId; // Link to User
    private String accountId; // The account affected
    private String transferAccountId; // Destination account (if type is TRANSFER)

    // Default Constructor
    public Transaction() {
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getTransferAccountId() {
        return transferAccountId;
    }

    public void setTransferAccountId(String transferAccountId) {
        this.transferAccountId = transferAccountId;
    }
}
