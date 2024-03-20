package com.example.nexign.parser;

import com.example.nexign.api.converter.Converter;
import com.example.nexign.api.parser.ObjectWriter;
import com.example.nexign.exception.FileWriteException;
import com.example.nexign.model.entity.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CsvTransactionWriter implements ObjectWriter<Transaction> {

    private final Converter<String, Transaction> converter;

    public String write(List<Transaction> transactions, String filename) {
        try {
            var path = Paths.get(filename);
            if (!Files.exists(path.getParent())) {
                Files.createDirectories(path.getParent());
            }
            if (!Files.exists(path)) {
                Files.createFile(path);
            }

            var csvContent = transactions.stream()
                    .map(transaction -> converter.convertTo(transaction) + "\n")
                    .collect(Collectors.joining());

            Files.writeString(path, csvContent);

            return filename;
        } catch (IOException e) {
            throw new FileWriteException("Unable to write CSV to file: " + filename);
        }
    }

}
