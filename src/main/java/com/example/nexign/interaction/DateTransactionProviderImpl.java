package com.example.nexign.interaction;

import com.example.nexign.api.interaction.DateTransactionProvider;
import com.example.nexign.api.parser.ObjectReader;
import com.example.nexign.api.service.CdrService;
import com.example.nexign.config.property.GeneratorProperties;
import com.example.nexign.model.entity.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DateTransactionProviderImpl implements DateTransactionProvider {

    private final CdrService cdrService;
    private final ObjectReader<Transaction> reader;
    private final GeneratorProperties generatorProperties;

    @Override
    public Map<LocalDate, Collection<Transaction>> provide() {
        var transactionsMap = new HashMap<LocalDate, Collection<Transaction>>();
        for (int i = generatorProperties.getMonthStart(); i <= generatorProperties.getMonthEnd(); i++) {
            var date = LocalDate.of(generatorProperties.getYear(), i, 1);
            var filename = cdrService.generateReport(date);
            if (filename.isEmpty()) {
                continue;
            }

            transactionsMap.put(date, reader.read(filename.get()));
        }

        return transactionsMap;
    }

    @Override
    public Map<LocalDate, Collection<Transaction>> provide(LocalDate date) {
        var transactionMap = new HashMap<LocalDate, Collection<Transaction>>();

        cdrService.generateReport(date).ifPresent((filename) -> transactionMap.put(date, reader.read(filename)));

        return transactionMap;
    }
}
