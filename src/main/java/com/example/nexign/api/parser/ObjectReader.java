package com.example.nexign.api.parser;

import java.util.List;

public interface ObjectReader<T> {

    List<T> read(String filename);

}
