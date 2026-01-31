package com.moneymanager.repository;

import com.moneymanager.model.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction, String> {
    List<Transaction> findByDateTimeBetween(LocalDateTime start, LocalDateTime end);
    List<Transaction> findByDivision(String division);
    List<Transaction> findByCategory(String category);
}
