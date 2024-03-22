package com.example.nexign.service;

import com.example.nexign.api.interaction.DateTransactionProvider;
import com.example.nexign.api.parser.ObjectWriter;
import com.example.nexign.config.BaseTest;
import com.example.nexign.config.property.GeneratorProperties;
import com.example.nexign.model.CustomerSummary;
import com.example.nexign.model.entity.Customer;
import com.example.nexign.model.entity.Transaction;
import com.example.nexign.utils.TimeUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.function.Consumer;

public class UdrServiceImplTest extends BaseTest {

    @MockBean
    private DateTransactionProvider dateTransactionProvider;
    @MockBean
    private ObjectWriter<CustomerSummary> objectWriter;
    @MockBean
    private Consumer<String> printer;
    @Autowired
    private GeneratorProperties generatorProperties;
    @MockBean
    private TimeUtils timeUtils;
    @Autowired
    private UdrServiceImpl udrService;

    private static Transaction transaction;

    @BeforeAll
    public static void init() {
        transaction = new Transaction();

        transaction.setId(1L);
        transaction.setStart(0L);
        transaction.setEnd(1000L);
        transaction.setType((short) 1);

        Customer customer = new Customer();
        customer.setNumber(123456789);
        transaction.setCustomer(customer);
    }

    @Test
    void generateGeneralReport_ReportGeneration() {
        var date = LocalDate.of(generatorProperties.getYear(),
                generatorProperties.getMonthStart(), 1);
        var transactionMap = new HashMap<LocalDate, Collection<Transaction>>();
        transactionMap.put(date, Collections.singletonList(transaction));

        Mockito.when(dateTransactionProvider.provide()).thenReturn(transactionMap);
        Mockito.when(timeUtils.formatSeconds(Mockito.anyLong())).thenReturn("00:00:00:00");

        udrService.generateReport();

        Mockito.verify(objectWriter).write(Mockito.anyString(), Mockito.any());
        Mockito.verify(printer).accept(Mockito.anyString());
    }

    @Test
    void generatePersonalReport_ReportGeneration() {
        var date = LocalDate.of(generatorProperties.getYear(),
                generatorProperties.getMonthStart(), 1);
        var transactionMap = new HashMap<LocalDate, Collection<Transaction>>();
        transactionMap.put(date, Collections.singletonList(transaction));

        Mockito.when(dateTransactionProvider.provide()).thenReturn(transactionMap);
        Mockito.when(timeUtils.formatSeconds(Mockito.anyLong())).thenReturn("00:00:00:00");

        udrService.generateReport(123456789);

        Mockito.verify(objectWriter).write(Mockito.anyString(), Mockito.any());
        Mockito.verify(printer).accept(Mockito.anyString());
    }

    @Test
    void generatePersonalAndMonthlyReport_ReportGenerated() {
        var date = LocalDate.of(generatorProperties.getYear(),
                generatorProperties.getMonthStart(), 1);
        var transactionMap = new HashMap<LocalDate, Collection<Transaction>>();
        transactionMap.put(date, Collections.singletonList(transaction));

        Mockito.when(dateTransactionProvider.provide(Mockito.any(LocalDate.class))).thenReturn(transactionMap);
        Mockito.when(timeUtils.formatSeconds(Mockito.anyLong())).thenReturn("00:00:00:00");

        udrService.generateReport(123456789, generatorProperties.getMonthStart());

        Mockito.verify(objectWriter).write(Mockito.anyString(), Mockito.any());
        Mockito.verify(printer).accept(Mockito.anyString());
    }

    @Test
    void generateGeneralReport_NoTransactions_NoReportGenerated() {
        var date = LocalDate.of(generatorProperties.getYear(),
                generatorProperties.getMonthStart(), 1);
        var transactionMap = new HashMap<LocalDate, Collection<Transaction>>();
        transactionMap.put(date, Collections.emptyList());

        Mockito.when(dateTransactionProvider.provide()).thenReturn(transactionMap);

        udrService.generateReport();

        Mockito.verifyNoInteractions(objectWriter);
        Mockito.verify(printer, Mockito.never()).accept(Mockito.anyString());
    }

    @Test
    void generatePersonalReport_NoTransactions_NoReportGenerated() {
        var date = LocalDate.of(generatorProperties.getYear(),
                generatorProperties.getMonthStart(), 1);
        var transactionMap = new HashMap<LocalDate, Collection<Transaction>>();
        transactionMap.put(date, Collections.emptyList());

        Mockito.when(dateTransactionProvider.provide()).thenReturn(transactionMap);

        udrService.generateReport(123456789);

        Mockito.verifyNoInteractions(objectWriter);
        Mockito.verify(printer, Mockito.never()).accept(Mockito.anyString());
    }

}
