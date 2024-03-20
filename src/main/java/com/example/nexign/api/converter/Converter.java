package com.example.nexign.api.converter;

/**
 * Interface defines the contract for converters that convert between two types.
 *
 * @param <T> the type to convert to
 * @param <E> the type to convert from
 */
public interface Converter<T, E> {

    /**
     * Converts an element of type E to type T.
     *
     * @param element the element to convert
     * @return the converted element of type T
     */
    T convertTo(E element);

    /**
     * Converts an element of type T to type E.
     *
     * @param element the element to convert
     * @return the converted element of type E
     */
    E convertFrom(T element);

}
