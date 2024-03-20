package com.example.nexign.api.service;

import com.example.nexign.model.entity.Transaction;

import java.util.List;

public interface TransactionService {

    Transaction save(Transaction transaction);

    List<Transaction> getTransactionsByPeriod(Long start, Long end);

}
