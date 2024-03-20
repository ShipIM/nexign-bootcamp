package com.example.nexign.parser;

import com.example.nexign.api.converter.Converter;
import com.example.nexign.api.parser.ObjectReader;
import com.example.nexign.exception.FileReadException;
import com.example.nexign.model.entity.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CsvTransactionReader implements ObjectReader<Transaction> {

    private final Converter<String, Transaction> converter;

    @Override
    public List<Transaction> read(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            return br.lines().map(converter::convertFrom).collect(Collectors.toList());
        } catch (IOException e) {
            throw new FileReadException("Unable to read CSV from file: " + filename);
        }
    }

}
