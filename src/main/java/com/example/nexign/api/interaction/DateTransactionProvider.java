package com.example.nexign.api.interaction;

import com.example.nexign.model.entity.Transaction;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;

/**
 * Interface defining the contract for providing a collection of transactions.
 */
public interface DateTransactionProvider {

    /**
     * Provides a map where keys represent dates and values represent collections of transactions occurred on those dates.
     *
     * @return A map of dates and corresponding transactions.
     */
    Map<LocalDate, Collection<Transaction>> provide();

    /**
     * Provides a map where keys represent dates within the specified month and values represent collections of transactions occurred on those dates.
     *
     * @param month The month for which transactions are provided.
     * @return A map of dates within the specified month and corresponding transactions.
     */
    Map<LocalDate, Collection<Transaction>> provide(LocalDate month);

}
