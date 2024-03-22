package com.example.nexign.api.repository;

import com.example.nexign.model.entity.ListedTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing ListedTransaction entities.
 */
@Repository
public interface ListedTransactionRepository extends JpaRepository<ListedTransaction, Long> {
}
