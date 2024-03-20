package com.example.nexign.api.interaction;

public interface Publisher<T> {

    void addSubscriber(Subscriber<T> subscriber);

    void removeSubscriber(Subscriber<T> subscriber);

    void notifySubscribers(T message);

}
