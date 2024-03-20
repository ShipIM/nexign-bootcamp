package com.example.nexign.api.parser;

import java.util.List;

public interface ObjectWriter<T> {

    String write(List<T> objects, String filename);

}
