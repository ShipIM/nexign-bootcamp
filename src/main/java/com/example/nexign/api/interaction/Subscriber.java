package com.example.nexign.api.interaction;

/**
 * Interface defining the contract for subscribers that receive messages of a specific type.
 *
 * @param <T> the type of messages to receive
 */
public interface Subscriber<T> {

    /**
     * Receives a message of the specified type.
     *
     * @param message the message to receive
     */
    void receive(T message);

}
