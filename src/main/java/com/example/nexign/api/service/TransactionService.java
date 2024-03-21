package com.example.nexign.api.service;

import com.example.nexign.model.entity.Transaction;

import java.util.Collection;

/**
 * Interface defining the contract for transaction-related operations.
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
     * @return a collection of transactions within the specified period
     */
    Collection<Transaction> getTransactionsByPeriod(Long start, Long end);

}
