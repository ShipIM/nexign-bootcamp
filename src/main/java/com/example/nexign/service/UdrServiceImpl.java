package com.example.nexign.service;

import com.example.nexign.api.parser.ObjectReader;
import com.example.nexign.api.parser.ObjectWriter;
import com.example.nexign.api.service.CdrService;
import com.example.nexign.api.service.UdrService;
import com.example.nexign.config.property.GeneratorProperties;
import com.example.nexign.model.CustomerSummary;
import com.example.nexign.model.entity.Transaction;
import com.example.nexign.utils.TimeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class UdrServiceImpl implements UdrService {

    private final CdrService cdrService;
    private final ObjectReader<Transaction> objectReader;
    private final ObjectWriter<CustomerSummary> objectWriter;
    private final Consumer<String> printer;
    private final GeneratorProperties generatorProperties;
    private final TimeUtils timeUtils;

    private final static String MAP_PATTERN = "%d_%d";
    private final static String PATH = "reports/%d_%s.json";

    @Override
    public void generateReport() {
        var transactionsMap = extractTransactionMap();
        var totalSummaryMap = new HashMap<Integer, CustomerSummary>();

        transactionsMap.forEach((k, v) -> extractSummaryMap(v).forEach((key, value) -> {
            var summary = totalSummaryMap.get(key);
            if (Objects.isNull(summary)) {
                totalSummaryMap.put(key, value);
            } else {
                summary.setOutcoming(summary.getOutcoming() + value.getOutcoming());
                summary.setIncoming(summary.getIncoming() + value.getIncoming());
            }

            objectWriter.write(String.format(PATH, key, k), value);
        }));

        logTotal(totalSummaryMap);
    }

    @Override
    public void generateReport(Integer msisdn) {
        var transactionMap = extractTransactionMap();
        var personalSummaryMap = processTransactions(transactionMap, msisdn);

        logPersonal(personalSummaryMap);
    }

    @Override
    public void generateReport(Integer msisdn, Integer month) {
        var transactionMap = new HashMap<String, Collection<Transaction>>();

        for (int i = generatorProperties.getYearStart(); i <= generatorProperties.getYearEnd(); i++) {
            var filename = cdrService.generateReport(LocalDate.of(i, month, 1));
            if (filename.isEmpty()) {
                continue;
            }

            transactionMap.put(String.format(MAP_PATTERN, i, month), objectReader.read(filename.get()));
        }

        var personalSummaryMap = processTransactions(transactionMap, msisdn);

        logPersonal(personalSummaryMap);
    }

    private Map<String, CustomerSummary> processTransactions(Map<String, Collection<Transaction>> transactionMap,
                                                             Integer msisdn) {
        var personalSummaryMap = new HashMap<String, CustomerSummary>();
        transactionMap.forEach((k, v) -> extractSummaryMap(v).entrySet().stream()
                .filter(entry -> entry.getKey().equals(msisdn))
                .forEach(entry -> {
                    personalSummaryMap.put(k, entry.getValue());
                    objectWriter.write(String.format(PATH, entry.getKey(), k), entry.getValue());
                }));

        return personalSummaryMap;
    }

    private Map<String, Collection<Transaction>> extractTransactionMap() {
        var transactionsMap = new HashMap<String, Collection<Transaction>>();
        for (int i = generatorProperties.getYearStart(); i <= generatorProperties.getYearEnd(); i++) {
            for (int j = generatorProperties.getMonthStart(); j <= generatorProperties.getMonthEnd(); j++) {
                var filename = cdrService.generateReport(LocalDate.of(i, j, 1));
                if (filename.isEmpty()) {
                    continue;
                }

                transactionsMap.put(String.format(MAP_PATTERN, i, j), objectReader.read(filename.get()));
            }
        }

        return transactionsMap;
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
