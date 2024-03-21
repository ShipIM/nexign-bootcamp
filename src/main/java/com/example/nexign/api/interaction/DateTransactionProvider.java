package com.example.nexign.api.interaction;

import com.example.nexign.model.entity.Transaction;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;

public interface DateTransactionProvider {

    Map<LocalDate, Collection<Transaction>> provide();

    Map<LocalDate, Collection<Transaction>> provide(LocalDate month);

}
