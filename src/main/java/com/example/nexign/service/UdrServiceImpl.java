package com.example.nexign.service;

import com.example.nexign.api.interaction.DateTransactionProvider;
import com.example.nexign.api.parser.ObjectWriter;
import com.example.nexign.api.service.UdrService;
import com.example.nexign.config.property.GeneratorProperties;
import com.example.nexign.model.CustomerSummary;
import com.example.nexign.model.entity.Transaction;
import com.example.nexign.utils.TimeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class UdrServiceImpl implements UdrService {

    private final DateTransactionProvider provider;
    private final ObjectWriter<CustomerSummary> objectWriter;
    private final Consumer<String> printer;
    private final GeneratorProperties generatorProperties;
    private final TimeUtils timeUtils;

    private final static String DATE_PATTERN = "MM";
    private final static String PATH = "reports/%d_%s.json";


    @Override
    public void generateReport() {
        var transactionsMap = provider.provide();
        var totalSummaryMap = new HashMap<Integer, CustomerSummary>();
        var formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);

        transactionsMap.forEach((k, v) -> extractSummaryMap(v).forEach((key, value) -> {
            var summary = totalSummaryMap.get(key);
            if (Objects.isNull(summary)) {
                totalSummaryMap.put(key, value);
            } else {
                summary.setOutcoming(summary.getOutcoming() + value.getOutcoming());
                summary.setIncoming(summary.getIncoming() + value.getIncoming());
            }

            objectWriter.write(String.format(PATH, key, k.format(formatter)), value);
        }));

        logTotal(totalSummaryMap);
    }

    @Override
    public void generateReport(Integer msisdn) {
        var transactionMap = provider.provide();
        var personalSummaryMap = processTransactions(transactionMap, msisdn);

        logPersonal(personalSummaryMap);
    }

    @Override
    public void generateReport(Integer msisdn, Integer month) {
        var date = LocalDate.of(generatorProperties.getYear(), month, 1);
        var transactionMap = provider.provide(date);

        var personalSummaryMap = processTransactions(transactionMap, msisdn);

        logPersonal(personalSummaryMap);
    }

    private Map<String, CustomerSummary> processTransactions(Map<LocalDate, Collection<Transaction>> transactionMap,
                                                             Integer msisdn) {
        var personalSummaryMap = new HashMap<String, CustomerSummary>();
        var formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
        transactionMap.forEach((k, v) -> extractSummaryMap(v).entrySet().stream()
                .filter(entry -> entry.getKey().equals(msisdn))
                .forEach(entry -> {
                    var month = k.format(formatter);
                    personalSummaryMap.put(month, entry.getValue());
                    objectWriter.write(String.format(PATH, entry.getKey(), month), entry.getValue());
                }));

        return personalSummaryMap;
    }

    private Map<Integer, CustomerSummary> extractSummaryMap(Collection<Transaction> transactions) {
        var summaryMap = new HashMap<Integer, CustomerSummary>();
        transactions.forEach(transaction -> {
            var number = transaction.getCustomer().getNumber();
            var summary = summaryMap.containsKey(number) ?
                    summaryMap.get(number) :
                    new CustomerSummary(number, 0L, 0L);

            if (transaction.getType() == 1) {
                summary.setOutcoming(
                        summary.getOutcoming() + transaction.getEnd() - transaction.getStart()
                );
            } else {
                summary.setIncoming(
                        summary.getIncoming() + transaction.getEnd() - transaction.getStart()
                );
            }

            summaryMap.put(number, summary);
        });

        return summaryMap;
    }

    private void logTotal(Map<Integer, CustomerSummary> totalSummaryMap) {
        if (totalSummaryMap.size() == 0) {
            return;
        }

        StringBuilder output = new StringBuilder("+-----------+--------------+--------------+\n");
        output.append("|   MSISDN  |   Incoming   |  Outcoming   |\n");
        output.append("+-----------+--------------+--------------+\n");
        totalSummaryMap.values().forEach(value -> output.append(String.format("| %-10d| %-13s| %-13s|\n",
                value.getMsisdn(), timeUtils.formatSeconds(value.getIncoming()),
                timeUtils.formatSeconds(value.getOutcoming()))));
        output.append("+-----------+--------------+--------------+\n");

        printer.accept(output.toString());
    }

    private void logPersonal(Map<String, CustomerSummary> personalSummaryMap) {
        if (personalSummaryMap.size() == 0) {
            return;
        }

        StringBuilder output = new StringBuilder("+-----------+--------------+--------------+\n");
        output.append("|  Period   |   Incoming   |  Outcoming   |\n");
        output.append("+-----------+--------------+--------------+\n");
        personalSummaryMap.forEach((key, value) -> output.append(String.format("| %-10s| %-13s| %-13s|\n",
                key, timeUtils.formatSeconds(value.getIncoming()),
                timeUtils.formatSeconds(value.getOutcoming()))));
        output.append("+-----------+--------------+--------------+\n");

        printer.accept(output.toString());
    }

}
