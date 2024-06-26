package com.example.nexign.api.interaction;

import java.util.Collection;

/**
 * Interface defining the contract for loading a data.
 *
 * @param <T> the type of elements in the data source
 */
public interface Source<T> {

    /**
     * Retrieves the data.
     *
     * @return a collection containing elements from the data source
     */
    Collection<T> load();

}
