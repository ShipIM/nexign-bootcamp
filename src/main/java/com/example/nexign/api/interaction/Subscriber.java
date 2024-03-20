package com.example.nexign.api.interaction;

public interface Subscriber<T> {

    void receive(T message);

}
