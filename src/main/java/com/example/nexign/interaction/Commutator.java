package com.example.nexign.interaction;

import com.example.nexign.api.interaction.Publisher;
import com.example.nexign.api.interaction.Subscriber;
import com.example.nexign.api.source.Source;
import com.example.nexign.model.entity.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * The Commutator class acts as a publisher for transactions, notifying its subscriber (CDR) of incoming calls.
 */
@Component
@RequiredArgsConstructor
public class Commutator implements Publisher<Transaction> {

    private final List<Subscriber<Transaction>> subscribers;
    private final Source<Transaction> source;

    @Override
    public void addSubscriber(Subscriber<Transaction> subscriber) {
        subscribers.add(subscriber);
    }

    @Override
    public void removeSubscriber(Subscriber<Transaction> subscriber) {
        subscribers.remove(subscriber);
    }

    @Override
    public void notifySubscribers(Transaction message) {
        subscribers.forEach(subscriber -> subscriber.receive(message));
    }

    /**
     * Retrieves transactions from the source and notifies subscriber.
     */
    public void commit() {
        source.provide().forEach(this::notifySubscribers);
    }

}
