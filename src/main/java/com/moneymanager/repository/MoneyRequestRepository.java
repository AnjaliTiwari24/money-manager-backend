package com.moneymanager.repository;

import com.moneymanager.model.MoneyRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MoneyRequestRepository extends MongoRepository<MoneyRequest, String> {
    List<MoneyRequest> findByFromUserId(String fromUserId);
    List<MoneyRequest> findByToUserId(String toUserId);
    List<MoneyRequest> findByStatus(String status);
    List<MoneyRequest> findByFromUserIdOrToUserId(String fromUserId, String toUserId);
}
