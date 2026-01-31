package com.moneymanager.controller;

import com.moneymanager.model.MoneyTransfer;
import com.moneymanager.service.MoneyTransferService;
import com.moneymanager.dto.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transfers")
public class MoneyTransferController {

    @Autowired
    private MoneyTransferService transferService;

    @PostMapping("/send")
    public ResponseEntity<?> sendMoney(@RequestBody MoneyTransfer transfer) {
        try {
            MoneyTransfer result = transferService.sendMoney(transfer);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new ErrorResponse("Error: " + e.getMessage()));
        }
    }

    @PostMapping("/initiate")
    public ResponseEntity<MoneyTransfer> initiateTransfer(@RequestBody MoneyTransfer transfer) {
        try {
            MoneyTransfer result = transferService.initiateTransfer(transfer);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<MoneyTransfer> getTransfer(@PathVariable String id) {
        try {
            MoneyTransfer transfer = transferService.getTransferById(id);
            return ResponseEntity.ok(transfer);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<MoneyTransfer>> getUserTransfers(@PathVariable String userId) {
        try {
            List<MoneyTransfer> transfers = transferService.getTransfersByUser(userId);
            return ResponseEntity.ok(transfers);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/sent/{userId}")
    public ResponseEntity<List<MoneyTransfer>> getSentTransfers(@PathVariable String userId) {
        try {
            List<MoneyTransfer> transfers = transferService.getSentTransfers(userId);
            return ResponseEntity.ok(transfers);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/received/{userId}")
    public ResponseEntity<List<MoneyTransfer>> getReceivedTransfers(@PathVariable String userId) {
        try {
            List<MoneyTransfer> transfers = transferService.getReceivedTransfers(userId);
            return ResponseEntity.ok(transfers);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/pending/{userId}")
    public ResponseEntity<List<MoneyTransfer>> getPendingTransfers(@PathVariable String userId) {
        try {
            List<MoneyTransfer> transfers = transferService.getPendingTransfers(userId);
            return ResponseEntity.ok(transfers);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTransfer(@PathVariable String id) {
        try {
            transferService.deleteTransfer(id);
            return ResponseEntity.ok("Transfer deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to delete transfer");
        }
    }
}
