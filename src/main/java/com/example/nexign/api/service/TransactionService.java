package com.example.nexign.api.service;

import com.example.nexign.model.entity.Transaction;

import java.util.List;

/**
 * Interface defines the contract for transaction-related operations.
 */
public interface TransactionService {

    /**
     * Saves a transaction.
     *
     * @param transaction the transaction to be saved
     * @return the saved transaction
     */
    Transaction save(Transaction transaction);

    /**
     * Retrieves transactions within the specified period.
     *
     * @param start the lower bound of the period
     * @param end   the upper bound of the period
     * @return a list of transactions within the specified period
     */
    List<Transaction> getTransactionsByPeriod(Long start, Long end);

}
