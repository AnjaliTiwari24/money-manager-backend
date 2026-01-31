package com.moneymanager.repository;

import com.moneymanager.model.MoneyTransfer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MoneyTransferRepository extends MongoRepository<MoneyTransfer, String> {
    List<MoneyTransfer> findByFromUserId(String fromUserId);
    List<MoneyTransfer> findByToUserId(String toUserId);
    List<MoneyTransfer> findByFromUserIdOrToUserId(String fromUserId, String toUserId);
    List<MoneyTransfer> findByStatus(String status);
}
