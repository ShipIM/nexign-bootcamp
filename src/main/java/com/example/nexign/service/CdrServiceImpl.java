package com.example.nexign.service;

import com.example.nexign.api.interaction.Subscriber;
import com.example.nexign.api.parser.ObjectWriter;
import com.example.nexign.api.service.CdrService;
import com.example.nexign.api.service.TransactionService;
import com.example.nexign.model.entity.Transaction;
import com.example.nexign.utils.TimeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CdrServiceImpl implements CdrService, Subscriber<Transaction> {

    private final TransactionService transactionService;
    private final ObjectWriter<Transaction> objectWriter;
    private final TimeUtils timeUtils;

    private final static String PATH = "cdr/%d_%d.txt";

    @Override
    public void receive(Transaction message) {
        transactionService.save(message);
    }

    public String generateReport(Integer year, Integer month) {
        var filename = String.format(PATH, year, month);
        var transactions = transactionService.getTransactionsByPeriod(
                timeUtils.getStartOfMonthUnixTime(year, month),
                timeUtils.getEndOfMonthUnixTime(year, month)
        );

        return objectWriter.write(transactions, filename);
    }

}
