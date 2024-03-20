package com.example.nexign.api.repository;

import com.example.nexign.model.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findAllByStartBetween(Long start, Long end);

    void deleteByStartBetween(Long start, Long end);

    @Modifying
    @Query("INSERT INTO ListedTransaction lt (lt.start, lt.end, lt.type, lt.customer.id) " +
            "SELECT t.start, t.end, t.type, t.customer.id FROM Transaction t " +
            "WHERE t.start BETWEEN :start AND :end")
    void transferTransactionsBetween(Long start, Long end);

}
