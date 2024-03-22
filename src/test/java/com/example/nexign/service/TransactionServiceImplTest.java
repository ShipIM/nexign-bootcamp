package com.example.nexign.service;

import com.example.nexign.api.repository.CustomerRepository;
import com.example.nexign.api.repository.ListedTransactionRepository;
import com.example.nexign.api.repository.TransactionRepository;
import com.example.nexign.config.BaseTest;
import com.example.nexign.model.entity.Customer;
import com.example.nexign.model.entity.ListedTransaction;
import com.example.nexign.model.entity.Transaction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Comparator;
import java.util.List;

public class TransactionServiceImplTest extends BaseTest {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private ListedTransactionRepository listedTransactionRepository;
    @Autowired
    private TransactionServiceImpl transactionService;

    @BeforeEach
    private void clean() {
        customerRepository.deleteAll();
        transactionRepository.deleteAll();
        listedTransactionRepository.deleteAll();
    }

    @Test
    public void saveTransaction_CustomerDoesNotExist_TransactionAndCustomerCreated() {
        var customer = new Customer();
        customer.setNumber(123456789);

        var transaction = createTransaction(customer, 1L, 1L, (short) 1);
        var savedTransaction = transactionService.save(transaction);

        Assertions.assertAll(
                () -> Assertions.assertTrue(customerRepository.existsById(savedTransaction.getCustomer().getId())),
                () -> Assertions.assertEquals(customer, savedTransaction.getCustomer()),
                () -> Assertions.assertTrue(transactionRepository.existsById(savedTransaction.getId())),
                () -> Assertions.assertEquals(transaction, savedTransaction)
        );
    }

    @Test
    public void saveTransaction_CustomerExists_TransactionCreated() {
        var customer = new Customer();
        customer.setNumber(123456789);
        var existingCustomer = customerRepository.save(customer);

        var transaction = createTransaction(customer, 1L, 1L, (short) 1);
        var savedTransaction = transactionService.save(transaction);

        Assertions.assertAll(
                () -> Assertions.assertEquals(existingCustomer, savedTransaction.getCustomer()),
                () -> Assertions.assertTrue(transactionRepository.existsById(savedTransaction.getId())),
                () -> Assertions.assertEquals(transaction, savedTransaction)
        );
    }

    @Test
    public void getTransactionsByPeriod_TransactionsRetrievedAndTransferred() {
        var customer = new Customer();
        customer.setNumber(123456789);

        var start = 5L;
        var end = 10L;

        var first = createTransaction(customer, start, end, (short) 1);
        var second = createTransaction(customer, start + 1, end + 1, (short) 2);
        var third = createTransaction(customer, start - 1, end, (short) 1);
        var fourth = createTransaction(customer, end + 1, end + 2, (short) 2);
        transactionRepository.saveAll(List.of(first, second, third, fourth));

        var transactions = transactionService.getTransactionsByPeriod(start, end).stream()
                .sorted(Comparator.comparing(Transaction::getStart))
                .toList();

        var remaining = transactionRepository.findAll().stream()
                .sorted(Comparator.comparing(Transaction::getStart))
                .toList();

        var deleted = listedTransactionRepository.findAll().stream()
                .sorted(Comparator.comparing(ListedTransaction::getStart))
                .toList();

        Assertions.assertAll(
                () -> Assertions.assertEquals(2, transactions.size()),
                () -> Assertions.assertAll(
                        () -> Assertions.assertEquals(start, transactions.get(0).getStart()),
                        () -> Assertions.assertEquals(start + 1, transactions.get(1).getStart())
                ),
                () -> Assertions.assertEquals(2, transactionRepository.count()),
                () -> Assertions.assertAll(
                        () -> Assertions.assertEquals(start - 1, remaining.get(0).getStart()),
                        () -> Assertions.assertEquals(end + 1, remaining.get(1).getStart())
                ),
                () -> Assertions.assertEquals(2, listedTransactionRepository.count()),
                () -> Assertions.assertAll(
                        () -> Assertions.assertEquals(start, deleted.get(0).getStart()),
                        () -> Assertions.assertEquals(start + 1, deleted.get(1).getStart())
                )
        );

    }

    private Transaction createTransaction(Customer customer, Long start, Long end, Short type) {
        var transaction = new Transaction();

        transaction.setCustomer(customer);
        transaction.setStart(start);
        transaction.setEnd(end);
        transaction.setType(type);

        return transaction;
    }


}
