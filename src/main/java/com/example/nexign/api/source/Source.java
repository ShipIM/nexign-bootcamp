package com.example.nexign.api.source;

import java.util.List;

/**
 * Interface is responsible for providing a data source.
 *
 * @param <T> the type of elements in the data source
 */
public interface Source<T> {

    /**
     * Retrieves the data source.
     *
     * @return a list containing elements from the data source
     */
    List<T> provide();

}
