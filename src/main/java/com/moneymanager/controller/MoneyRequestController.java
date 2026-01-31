package com.moneymanager.controller;

import com.moneymanager.model.MoneyRequest;
import com.moneymanager.service.MoneyRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
public class MoneyRequestController {

    @Autowired
    private MoneyRequestService requestService;

    @PostMapping("/create")
    public ResponseEntity<MoneyRequest> createRequest(@RequestBody MoneyRequest request) {
        try {
            MoneyRequest result = requestService.createRequest(request);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/{id}/accept")
    public ResponseEntity<MoneyRequest> acceptRequest(
            @PathVariable String id,
            @RequestParam String accountId) {
        try {
            MoneyRequest result = requestService.acceptRequest(id, accountId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<MoneyRequest> rejectRequest(@PathVariable String id) {
        try {
            MoneyRequest result = requestService.rejectRequest(id);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<MoneyRequest> getRequest(@PathVariable String id) {
        try {
            MoneyRequest request = requestService.getRequestById(id);
            return ResponseEntity.ok(request);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<MoneyRequest>> getUserRequests(@PathVariable String userId) {
        try {
            List<MoneyRequest> requests = requestService.getRequestsByUser(userId);
            return ResponseEntity.ok(requests);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/sent/{userId}")
    public ResponseEntity<List<MoneyRequest>> getSentRequests(@PathVariable String userId) {
        try {
            List<MoneyRequest> requests = requestService.getSentRequests(userId);
            return ResponseEntity.ok(requests);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/received/{userId}")
    public ResponseEntity<List<MoneyRequest>> getReceivedRequests(@PathVariable String userId) {
        try {
            List<MoneyRequest> requests = requestService.getReceivedRequests(userId);
            return ResponseEntity.ok(requests);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/pending/{userId}")
    public ResponseEntity<List<MoneyRequest>> getPendingRequests(@PathVariable String userId) {
        try {
            List<MoneyRequest> requests = requestService.getPendingRequests(userId);
            return ResponseEntity.ok(requests);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRequest(@PathVariable String id) {
        try {
            requestService.deleteRequest(id);
            return ResponseEntity.ok("Request deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to delete request");
        }
    }
}
