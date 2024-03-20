package com.example.nexign.api.parser;

import java.util.List;

/**
 * Interface defines the contract for writing objects to a file.
 *
 * @param <T> the type of objects to be written
 */
public interface ObjectWriter<T> {

    /**
     * Writes a list of objects to a file.
     *
     * @param objects  the list of objects to write
     * @param filename the name of the file to write to
     * @return the filename of the written file
     */
    String write(List<T> objects, String filename);

}
