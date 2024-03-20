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
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JsonSummaryWriter implements ObjectWriter<CustomerSummary> {

    private final Converter<CustomerSummaryPresenter, CustomerSummary> converter;

    @Override
    public String write(List<CustomerSummary> objects, String filename) {
        try {
            var path = Paths.get(filename);
            if (!Files.exists(path.getParent())) {
                Files.createDirectories(path.getParent());
            }
            if (!Files.exists(path)) {
                Files.createFile(path);
            }

            var mapper = new ObjectMapper();

            Files.writeString(path, mapper.writeValueAsString(objects.stream()
                    .map(converter::convertTo).collect(Collectors.toList())));

            return filename;
        } catch (IOException e) {
            throw new FileWriteException("Unable to write JSON to file: " + filename);
        }
    }

}
