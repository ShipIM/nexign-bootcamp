package com.example.nexign.api.interaction;

/**
 * Interface defining the contract for publishers that manage subscribers and notify them of messages.
 *
 * @param <T> the type of messages to publish
 */
public interface Publisher<T> {

    /**
     * Adds a subscriber to the publisher.
     *
     * @param subscriber the subscriber to add
     */
    void addSubscriber(Subscriber<T> subscriber);

    /**
     * Removes a subscriber from the publisher.
     *
     * @param subscriber the subscriber to remove
     */
    void removeSubscriber(Subscriber<T> subscriber);

    /**
     * Notifies all subscribers with the given message.
     *
     * @param message the message to notify subscribers with
     */
    void notifySubscribers(T message);

}
