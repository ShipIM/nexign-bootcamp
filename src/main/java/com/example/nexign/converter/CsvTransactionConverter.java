package com.example.nexign.converter;

import com.example.nexign.api.converter.Converter;
import com.example.nexign.model.entity.Customer;
import com.example.nexign.model.entity.Transaction;
import org.springframework.stereotype.Component;

@Component
public class CsvTransactionConverter implements Converter<String, Transaction> {

    private final static String PATTERN = "0%d,%d,%d,%d";

    @Override
    public String convertTo(Transaction element) {
        return String.format(PATTERN,
                element.getType(), element.getCustomer().getNumber(), element.getStart(), element.getEnd());
    }

    @Override
    public Transaction convertFrom(String element) {
        var parts = element.split(",");

        var transaction = new Transaction();
        var customer = new Customer();

        transaction.setType(Short.parseShort(parts[0]));
        customer.setNumber(Integer.parseInt(parts[1]));
        transaction.setCustomer(customer);
        transaction.setStart(Long.parseLong(parts[2]));
        transaction.setEnd(Long.parseLong(parts[3]));

        return transaction;
    }

}
