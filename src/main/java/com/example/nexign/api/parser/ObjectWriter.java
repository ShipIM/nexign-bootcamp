package com.example.nexign.api.parser;

import java.util.Optional;

/**
 * Interface defining the contract for writing objects to a file.
 *
 * @param <T> the type of objects to be written
 */
public interface ObjectWriter<T> {

    /**
     * Writes a list of objects to a file.
     *
     * @param filename the name of the file to write to
     * @param objects  the list of objects to write
     * @return an Optional containing the filename of the written file,
     *         or an empty Optional if the write operation fails
     */
    Optional<String> write(String filename, T... objects);

}
