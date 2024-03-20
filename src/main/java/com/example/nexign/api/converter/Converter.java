package com.example.nexign.api.converter;

public interface Converter<T, E> {

    T convertTo(E element);

    E convertFrom(T element);

}
