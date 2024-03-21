package com.example.nexign.api.parser;

import java.util.Collection;

/**
 * Interface defining the contract for reading objects from a file.
 *
 * @param <T> the type of objects to be read
 */
public interface ObjectReader<T> {

    /**
     * Reads objects from a file.
     *
     * @param filename the name of the file to read from
     * @return a collection of objects read from the file
     */
    Collection<T> read(String filename);

}
