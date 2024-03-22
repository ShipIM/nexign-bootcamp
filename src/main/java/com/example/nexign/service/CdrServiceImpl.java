package com.example.nexign.service;

import com.example.nexign.api.interaction.Subscriber;
import com.example.nexign.api.parser.ObjectWriter;
import com.example.nexign.api.service.CdrService;
import com.example.nexign.api.service.TransactionService;
import com.example.nexign.model.entity.Transaction;
import com.example.nexign.utils.TimeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CdrServiceImpl implements CdrService, Subscriber<Transaction> {

    private final TransactionService transactionService;
    private final ObjectWriter<Transaction> objectWriter;
    private final TimeUtils timeUtils;

    private final static String PATH = "cdr/%s.txt";
    public final static String DATE_PATTERN = "MM";

    @Override
    public void receive(Transaction message) {
        transactionService.save(message);
    }

    public Optional<String> generateReport(LocalDate date) {
        var formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
        var filename = String.format(PATH, date.format(formatter));
        var transactions = transactionService.getTransactionsByPeriod(
                timeUtils.getStartOfMonthUnixTime(date), timeUtils.getEndOfMonthUnixTime(date));

        return objectWriter.write(filename, transactions.toArray(Transaction[]::new));
    }

}
