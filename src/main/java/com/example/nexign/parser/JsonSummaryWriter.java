package com.example.nexign.parser;

import com.example.nexign.api.converter.Converter;
import com.example.nexign.api.parser.ObjectWriter;
import com.example.nexign.exception.FileWriteException;
import com.example.nexign.model.CustomerSummary;
import com.example.nexign.presenter.CustomerSummaryPresenter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JsonSummaryWriter implements ObjectWriter<CustomerSummary> {

    private final Converter<CustomerSummaryPresenter, CustomerSummary> converter;

    @Override
    public Optional<String> write(String filename, CustomerSummary... objects) {
        if (objects.length == 0) {
            return Optional.empty();
        }

        try {
            var path = Paths.get(filename);
            if (!Files.exists(path.getParent())) {
                Files.createDirectories(path.getParent());
            }
            if (!Files.exists(path)) {
                Files.createFile(path);
            }

            var mapper = new ObjectMapper();
            var summary = objects[0];

            Files.writeString(path, mapper.writerWithDefaultPrettyPrinter().
                    writeValueAsString(converter.convertTo(summary)));

            return Optional.of(filename);
        } catch (IOException e) {
            throw new FileWriteException("Unable to write JSON to file: " + filename);
        }
    }

}
