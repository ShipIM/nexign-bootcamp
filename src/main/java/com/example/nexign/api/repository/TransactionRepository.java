package com.example.nexign.api.repository;

import com.example.nexign.model.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing Transaction entities.
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    /**
     * Retrieves transactions with lower bound within the specified range.
     *
     * @param start the lower bound of the range
     * @param end   the upper bound of the range
     * @return a collection of transactions within the specified range
     */
    List<Transaction> findAllByStartBetween(Long start, Long end);

    /**
     * Deletes transactions with lower bound within the specified range.
     *
     * @param start the lower bound of the range
     * @param end   the upper bound of the range
     */
    void deleteByStartBetween(Long start, Long end);

    /**
     * Transfers transactions with lower bound within the specified range to the ListedTransaction table.
     *
     * @param start the lower bound of the range
     * @param end   the upper bound of the range
     */
    @Modifying
    @Query("INSERT INTO ListedTransaction lt (lt.start, lt.end, lt.type, lt.customer.id) " +
            "SELECT t.start, t.end, t.type, t.customer.id FROM Transaction t " +
            "WHERE t.start BETWEEN :start AND :end")
    void transferTransactionsBetween(Long start, Long end);

}
