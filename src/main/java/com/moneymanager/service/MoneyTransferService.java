package com.moneymanager.service;

import com.moneymanager.model.MoneyTransfer;
import com.moneymanager.model.Account;
import com.moneymanager.model.Transaction;
import com.moneymanager.repository.MoneyTransferRepository;
import com.moneymanager.repository.AccountRepository;
import com.moneymanager.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MoneyTransferService {

    @Autowired
    private MoneyTransferRepository transferRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public MoneyTransfer sendMoney(MoneyTransfer transfer) {
        // Validate sender's account has sufficient balance
        Optional<Account> fromAccount = accountRepository.findById(transfer.getFromAccountId());
        if (fromAccount.isEmpty() || fromAccount.get().getBalance() < transfer.getAmount()) {
            throw new RuntimeException("Insufficient balance");
        }

        // Validate receiver's account exists
        Optional<Account> toAccount = accountRepository.findById(transfer.getToAccountId());
        if (toAccount.isEmpty()) {
            throw new RuntimeException("Receiver account not found");
        }

        // Deduct from sender's account
        Account sender = fromAccount.get();
        sender.setBalance(sender.getBalance() - transfer.getAmount());
        accountRepository.save(sender);

        // Add to receiver's account
        Account receiver = toAccount.get();
        receiver.setBalance(receiver.getBalance() + transfer.getAmount());
        accountRepository.save(receiver);

        // Save transfer record
        transfer.setStatus("COMPLETED");
        transfer.setCompletedAt(LocalDateTime.now());
        if (transfer.getCreatedAt() == null) {
            transfer.setCreatedAt(LocalDateTime.now());
        }
        MoneyTransfer savedTransfer = transferRepository.save(transfer);

        // Create transaction records (EXPENSE for sender, INCOME for receiver)
        LocalDateTime now = LocalDateTime.now();
        
        // Sender's EXPENSE transaction
        Transaction senderTransaction = new Transaction();
        senderTransaction.setType("EXPENSE");
        senderTransaction.setAmount(transfer.getAmount());
        senderTransaction.setDateTime(now);
        senderTransaction.setDescription("Transfer to " + receiver.getName() + ": " + transfer.getDescription());
        senderTransaction.setCategory("Transfer");
        senderTransaction.setDivision("PERSONAL");
        senderTransaction.setUserId(transfer.getFromUserId());
        senderTransaction.setAccountId(transfer.getFromAccountId());
        transactionRepository.save(senderTransaction);

        // Receiver's INCOME transaction
        Transaction receiverTransaction = new Transaction();
        receiverTransaction.setType("INCOME");
        receiverTransaction.setAmount(transfer.getAmount());
        receiverTransaction.setDateTime(now);
        receiverTransaction.setDescription("Transfer from " + sender.getName() + ": " + transfer.getDescription());
        receiverTransaction.setCategory("Transfer");
        receiverTransaction.setDivision("PERSONAL");
        receiverTransaction.setUserId(transfer.getToUserId());
        receiverTransaction.setAccountId(transfer.getToAccountId());
        transactionRepository.save(receiverTransaction);

        return savedTransfer;
    }

    public MoneyTransfer initiateTransfer(MoneyTransfer transfer) {
        transfer.setStatus("PENDING");
        transfer.setCreatedAt(LocalDateTime.now());
        return transferRepository.save(transfer);
    }

    public MoneyTransfer getTransferById(String id) {
        return transferRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transfer not found"));
    }

    public List<MoneyTransfer> getTransfersByUser(String userId) {
        return transferRepository.findByFromUserIdOrToUserId(userId, userId);
    }

    public List<MoneyTransfer> getSentTransfers(String userId) {
        return transferRepository.findByFromUserId(userId);
    }

    public List<MoneyTransfer> getReceivedTransfers(String userId) {
        return transferRepository.findByToUserId(userId);
    }

    public List<MoneyTransfer> getPendingTransfers(String userId) {
        List<MoneyTransfer> pending = transferRepository.findByStatus("PENDING");
        return pending.stream()
                .filter(t -> t.getFromUserId().equals(userId) || t.getToUserId().equals(userId))
                .toList();
    }

    public void deleteTransfer(String id) {
        transferRepository.deleteById(id);
    }
}
