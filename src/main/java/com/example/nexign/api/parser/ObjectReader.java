package com.example.nexign.api.parser;

import java.util.List;

/**
 * Interface defines the contract for reading objects from a file.
 *
 * @param <T> the type of objects to be read
 */
public interface ObjectReader<T> {

    /**
     * Reads objects from a file.
     *
     * @param filename the name of the file to read from
     * @return a list of objects read from the file
     */
    List<T> read(String filename);

}
