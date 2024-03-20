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

import java.util.*;

@Service
@RequiredArgsConstructor
public class UdrServiceImpl implements UdrService {

    private final CdrService cdrService;
    private final ObjectReader<Transaction> objectReader;
    private final ObjectWriter<CustomerSummary> objectWriter;
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

            objectWriter.write(Collections.singletonList(value), String.format(PATH, key, k));
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
        var transactionMap = new HashMap<String, List<Transaction>>();

        for (int i = generatorProperties.getYearStart(); i <= generatorProperties.getYearEnd(); i++) {
            var filename = cdrService.generateReport(i, month);
            transactionMap.put(String.format(MAP_PATTERN, month, i), objectReader.read(filename));
        }

        var personalSummaryMap = processTransactions(transactionMap, msisdn);

        logPersonal(personalSummaryMap);
    }

    private Map<String, CustomerSummary> processTransactions(Map<String, List<Transaction>> transactionMap,
                                                             Integer msisdn) {
        var personalSummaryMap = new HashMap<String, CustomerSummary>();
        transactionMap.forEach((k, v) -> extractSummaryMap(v).entrySet().stream()
                .filter(entry -> entry.getKey().equals(msisdn))
                .forEach(entry -> {
                    personalSummaryMap.put(k, entry.getValue());
                    objectWriter.write(Collections.singletonList(entry.getValue()),
                            String.format(PATH, entry.getKey(), k));
                }));

        return personalSummaryMap;
    }

    private Map<String, List<Transaction>> extractTransactionMap() {
        var transactionsMap = new HashMap<String, List<Transaction>>();
        for (int i = generatorProperties.getYearStart(); i <= generatorProperties.getYearEnd(); i++) {
            for (int j = generatorProperties.getMonthStart(); j <= generatorProperties.getMonthEnd(); j++) {
                var filename = cdrService.generateReport(i, j);
                transactionsMap.put(String.format(MAP_PATTERN, j, i), objectReader.read(filename));
            }
        }

        return transactionsMap;
    }

    private Map<Integer, CustomerSummary> extractSummaryMap(List<Transaction> transactions) {
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

        System.out.println("+-----------+--------------+--------------+");
        System.out.println("|   MSISDN  |   Incoming   |  Outcoming   |");
        System.out.println("+-----------+--------------+--------------+");
        totalSummaryMap.values().forEach(value -> System.out.printf("| %-10d| %-13s| %-13s|%n",
                value.getMsisdn(), timeUtils.formatSeconds(value.getIncoming()),
                timeUtils.formatSeconds(value.getOutcoming())));
        System.out.println("+-----------+--------------+--------------+");
    }

    private void logPersonal(Map<String, CustomerSummary> personalSummaryMap) {
        if (personalSummaryMap.size() == 0) {
            return;
        }

        System.out.println("+-----------+--------------+--------------+");
        System.out.println("|  Period   |   Incoming   |  Outcoming   |");
        System.out.println("+-----------+--------------+--------------+");
        personalSummaryMap.forEach((key, value) -> System.out.printf("| %-10s| %-13s| %-13s|%n",
                key, timeUtils.formatSeconds(value.getIncoming()),
                timeUtils.formatSeconds(value.getOutcoming())));
        System.out.println("+-----------+--------------+--------------+");
    }

}
