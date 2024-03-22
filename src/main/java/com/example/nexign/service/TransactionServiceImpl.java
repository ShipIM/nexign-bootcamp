package com.example.nexign.service;

import com.example.nexign.api.repository.CustomerRepository;
import com.example.nexign.api.service.TransactionService;
import com.example.nexign.model.entity.Transaction;
import com.example.nexign.api.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final CustomerRepository customerRepository;
    private final TransactionRepository transactionRepository;

    @Transactional
    public Transaction save(Transaction transaction) {
        customerRepository.findByNumber(transaction.getCustomer().getNumber())
                .ifPresent(transaction::setCustomer);

        return transactionRepository.save(transaction);
    }

    @Transactional
    public Collection<Transaction> getTransactionsByPeriod(Long start, Long end) {
        var transactions = transactionRepository.findAllByStartBetween(start, end);

        transactionRepository.transferTransactionsBetween(start, end);
        transactionRepository.deleteByStartBetween(start, end);

        return transactions;
    }

}
