package com.moneymanager.service;

import com.moneymanager.model.Transaction;
import com.moneymanager.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountService accountService;

    public List<Transaction> getAllTransactions() {
        // TODO: In real app, filter by User. For now, we rely on Frontend filtering or
        // update Controller.
        return transactionRepository.findAll();
    }

    public List<Transaction> getTransactionsByUserId(String userId) {
        return transactionRepository.findAll().stream()
                .filter(t -> userId.equals(t.getUserId()))
                .toList();
    }

    @Transactional
    public Transaction addTransaction(Transaction transaction) {
        if (transaction.getDateTime() == null) {
            transaction.setDateTime(LocalDateTime.now());
        }

        // Update Account Balances
        if ("INCOME".equalsIgnoreCase(transaction.getType())) {
            accountService.updateBalance(transaction.getAccountId(), transaction.getAmount());
        } else if ("EXPENSE".equalsIgnoreCase(transaction.getType())) {
            accountService.updateBalance(transaction.getAccountId(), -transaction.getAmount());
        } else if ("TRANSFER".equalsIgnoreCase(transaction.getType())) {
            // Deduct from Source
            accountService.updateBalance(transaction.getAccountId(), -transaction.getAmount());
            // Add to Destination
            if (transaction.getTransferAccountId() != null) {
                accountService.updateBalance(transaction.getTransferAccountId(), transaction.getAmount());
            }
        }

        return transactionRepository.save(transaction);
    }

    @Transactional
    public Transaction updateTransaction(String id, Transaction transactionDetails) {
        Optional<Transaction> optionalTransaction = transactionRepository.findById(id);
        if (optionalTransaction.isPresent()) {
            Transaction existingTransaction = optionalTransaction.get();

            // Revert Old Balance Impact
            revertBalance(existingTransaction);

            // Update Fields
            existingTransaction.setAmount(transactionDetails.getAmount());
            existingTransaction.setCategory(transactionDetails.getCategory());
            existingTransaction.setDescription(transactionDetails.getDescription());
            existingTransaction.setDivision(transactionDetails.getDivision());
            existingTransaction.setType(transactionDetails.getType());
            existingTransaction.setAccountId(transactionDetails.getAccountId());
            existingTransaction.setTransferAccountId(transactionDetails.getTransferAccountId());

            // Apply New Balance Impact
            if ("INCOME".equalsIgnoreCase(existingTransaction.getType())) {
                accountService.updateBalance(existingTransaction.getAccountId(), existingTransaction.getAmount());
            } else if ("EXPENSE".equalsIgnoreCase(existingTransaction.getType())) {
                accountService.updateBalance(existingTransaction.getAccountId(), -existingTransaction.getAmount());
            } else if ("TRANSFER".equalsIgnoreCase(existingTransaction.getType())) {
                accountService.updateBalance(existingTransaction.getAccountId(), -existingTransaction.getAmount());
                if (existingTransaction.getTransferAccountId() != null) {
                    accountService.updateBalance(existingTransaction.getTransferAccountId(),
                            existingTransaction.getAmount());
                }
            }

            return transactionRepository.save(existingTransaction);
        } else {
            throw new RuntimeException("Transaction not found");
        }
    }

    @Transactional
    public void deleteTransaction(String id) {
        Optional<Transaction> optionalTransaction = transactionRepository.findById(id);
        if (optionalTransaction.isPresent()) {
            revertBalance(optionalTransaction.get());
            transactionRepository.deleteById(id);
        }
    }

    // Helper to Revert (Undo) a transaction's effect on balance
    private void revertBalance(Transaction t) {
        if ("INCOME".equalsIgnoreCase(t.getType())) {
            accountService.updateBalance(t.getAccountId(), -t.getAmount());
        } else if ("EXPENSE".equalsIgnoreCase(t.getType())) {
            accountService.updateBalance(t.getAccountId(), t.getAmount());
        } else if ("TRANSFER".equalsIgnoreCase(t.getType())) {
            accountService.updateBalance(t.getAccountId(), t.getAmount()); // Add back to source
            if (t.getTransferAccountId() != null) {
                accountService.updateBalance(t.getTransferAccountId(), -t.getAmount()); // Remove from dest
            }
        }
    }

    public List<Transaction> getTransactionsByDateRange(LocalDateTime start, LocalDateTime end) {
        return transactionRepository.findByDateTimeBetween(start, end);
    }
}
