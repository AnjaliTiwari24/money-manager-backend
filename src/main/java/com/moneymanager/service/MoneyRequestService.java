package com.moneymanager.service;

import com.moneymanager.model.MoneyRequest;
import com.moneymanager.model.Account;
import com.moneymanager.repository.MoneyRequestRepository;
import com.moneymanager.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MoneyRequestService {

    @Autowired
    private MoneyRequestRepository requestRepository;

    @Autowired
    private AccountRepository accountRepository;

    public MoneyRequest createRequest(MoneyRequest request) {
        request.setStatus("PENDING");
        request.setCreatedAt(LocalDateTime.now());
        return requestRepository.save(request);
    }

    public MoneyRequest acceptRequest(String requestId, String accountId) {
        MoneyRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        // Validate requester's account has sufficient balance
        Optional<Account> account = accountRepository.findById(accountId);
        if (account.isEmpty() || account.get().getBalance() < request.getAmount()) {
            throw new RuntimeException("Insufficient balance to fulfill request");
        }

        // Deduct from requester's account
        Account acc = account.get();
        acc.setBalance(acc.getBalance() - request.getAmount());
        accountRepository.save(acc);

        request.setStatus("PAID");
        request.setResolvedAt(LocalDateTime.now());
        return requestRepository.save(request);
    }

    public MoneyRequest rejectRequest(String requestId) {
        MoneyRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        request.setStatus("REJECTED");
        request.setResolvedAt(LocalDateTime.now());
        return requestRepository.save(request);
    }

    public MoneyRequest getRequestById(String id) {
        return requestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));
    }

    public List<MoneyRequest> getRequestsByUser(String userId) {
        return requestRepository.findByFromUserIdOrToUserId(userId, userId);
    }

    public List<MoneyRequest> getSentRequests(String userId) {
        return requestRepository.findByFromUserId(userId);
    }

    public List<MoneyRequest> getReceivedRequests(String userId) {
        return requestRepository.findByToUserId(userId);
    }

    public List<MoneyRequest> getPendingRequests(String userId) {
        return requestRepository.findByToUserId(userId).stream()
                .filter(r -> r.getStatus().equals("PENDING"))
                .toList();
    }

    public void deleteRequest(String id) {
        requestRepository.deleteById(id);
    }
}
