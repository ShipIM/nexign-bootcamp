package com.example.nexign.service;

import com.example.nexign.api.parser.ObjectWriter;
import com.example.nexign.api.service.TransactionService;
import com.example.nexign.config.BaseTest;
import com.example.nexign.config.property.GeneratorProperties;
import com.example.nexign.model.entity.Transaction;
import com.example.nexign.utils.TimeUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class CdrServiceImplTest extends BaseTest {

    @MockBean
    private TransactionService transactionService;
    @MockBean
    private ObjectWriter<Transaction> objectWriter;
    @MockBean
    private TimeUtils timeUtils;
    @Autowired
    private GeneratorProperties generatorProperties;
    @Autowired
    private CdrServiceImpl cdrService;

    @Test
    void receive_ValidTransaction_TransactionSaved() {
        Transaction transaction = new Transaction();

        cdrService.receive(transaction);

        Mockito.verify(transactionService, Mockito.times(1)).save(transaction);
    }

    @Test
    void generateReport_TransactionsRetrievedAndReportGenerated() {
        var date = LocalDate.of(generatorProperties.getYear(),
                generatorProperties.getMonthStart(), 1);
        var startOfMonth = 0L;
        var endOfMonth = 1L;
        var transactions = List.of(new Transaction(), new Transaction());

        var formatter = DateTimeFormatter.ofPattern(CdrServiceImpl.DATE_PATTERN);
        var filename = String.format("cdr/%s.txt", date.format(formatter));

        Mockito.when(timeUtils.getStartOfMonthUnixTime(date)).thenReturn(startOfMonth);
        Mockito.when(timeUtils.getEndOfMonthUnixTime(date)).thenReturn(endOfMonth);
        Mockito.when(transactionService.getTransactionsByPeriod(startOfMonth, endOfMonth)).thenReturn(transactions);
        Mockito.when(objectWriter.write(filename, transactions.toArray(Transaction[]::new)))
                .thenReturn(Optional.of(filename));

        Optional<String> result = cdrService.generateReport(date);

        Assertions.assertAll(
                () -> Assertions.assertTrue(result.isPresent()),
                () -> Assertions.assertEquals(filename, result.get())
        );

    }

}
