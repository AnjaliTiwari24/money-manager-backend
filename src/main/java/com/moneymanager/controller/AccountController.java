package com.moneymanager.controller;

import com.moneymanager.model.Account;
import com.moneymanager.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/user/{userId}")
    public List<Account> getUserAccounts(@PathVariable String userId) {
        return accountService.getAccountsByUserId(userId);
    }

    @PostMapping
    public Account createAccount(@RequestBody Account account) {
        System.out.println("Creating Account: " + account.getName() + ", PIN Present: "
                + (account.getPin() != null && !account.getPin().isEmpty()));
        return accountService.createAccount(account);
    }

    @DeleteMapping("/{id}")
    public void deleteAccount(@PathVariable String id) {
        accountService.deleteAccount(id);
    }
}
