package com.example.nexign.api.source;

import java.util.Collection;

/**
 * Interface is responsible for providing a data.
 *
 * @param <T> the type of elements in the data source
 */
public interface Source<T> {

    /**
     * Retrieves the data.
     *
     * @return a collection containing elements from the data source
     */
    Collection<T> provide();

}
