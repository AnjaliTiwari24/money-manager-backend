package com.moneymanager.controller;

import com.moneymanager.model.Transaction;
import com.moneymanager.repository.TransactionRepository;
import com.moneymanager.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "http://localhost:5173") // Allow React frontend
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransactionRepository transactionRepository;

    @GetMapping
    public List<Transaction> getAllTransactions(@RequestParam(required = false) String userId) {
        if (userId != null) {
            return transactionService.getTransactionsByUserId(userId);
        }
        return transactionService.getAllTransactions(); // Fallback (or restrict only to admin)
    }

    @PostMapping
    public Transaction createTransaction(@RequestBody Transaction transaction) {
        System.out.println("Received Transaction Request: " + transaction);
        System.out.println("Details: Amount=" + transaction.getAmount() + ", Desc=" + transaction.getDescription());
        return transactionService.addTransaction(transaction);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTransaction(@PathVariable String id, @RequestBody Transaction transactionDetails) {
        try {
            Transaction updatedTransaction = transactionService.updateTransaction(id, transactionDetails);
            return ResponseEntity.ok(updatedTransaction);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTransaction(@PathVariable String id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.ok().build();
    }
}
