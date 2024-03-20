package com.example.nexign.config;

import com.example.nexign.api.source.Source;
import com.example.nexign.config.property.GeneratorProperties;
import com.example.nexign.model.entity.Customer;
import com.example.nexign.model.entity.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.IntStream;

@Configuration
@EnableConfigurationProperties(value = GeneratorProperties.class)
@RequiredArgsConstructor
public class ApplicationConfig {

    private final GeneratorProperties generatorProperties;

    private final static Integer FEBRUARY = 28;

    @Bean
    public Consumer<String> printer() {
        return System.out::println;
    }

    @Bean
    public Source<Transaction> listTransactionSource(List<Transaction> transactions) {
        return () -> transactions;
    }

    @Bean
    public List<Transaction> transactions() {
        var transactions = new ArrayList<Transaction>();
        var random = new Random();

        var customers = generateCustomers(generatorProperties.getCustomers());

        for (int i = 0; i < generatorProperties.getTransactions(); i++) {
            var transaction = new Transaction();

            var start = generateRandomTime(random, generatorProperties.getYearStart(),
                    generatorProperties.getYearEnd(), generatorProperties.getMonthStart(),
                    generatorProperties.getMonthEnd());
            transaction.setStart(start);
            transaction.setEnd(start + random.nextInt(3600) * 1000);
            transaction.setType((short) (1 + random.nextInt(2)));

            var customer = customers.get(random.nextInt(customers.size()));
            transaction.setCustomer(customer);

            transactions.add(transaction);
        }

        return transactions;
    }

    private List<Customer> generateCustomers(Integer number) {
        var customers = new ArrayList<Customer>();

        IntStream.range(0, number).forEach(i -> {
            var customer = new Customer();
            customer.setNumber(100000000 + i);
            customers.add(customer);
        });

        return customers;
    }

    private Long generateRandomTime(Random random, Integer startYear, Integer endYear, Integer startMonth,
                                    Integer endMonth) {
        var year = startYear + random.nextInt(endYear - startYear + 1);
        var month = startMonth + random.nextInt(endMonth - startMonth + 1);
        var day = 1 + random.nextInt(FEBRUARY);

        return generateRandomTimestamp(random, year, month, day);
    }

    private Long generateRandomTimestamp(Random random, Integer year, Integer month, Integer day) {
        var minDay = LocalDate.of(year, month, day).toEpochDay();
        var maxDay = LocalDate.of(year, month, Month.of(month).length(Year.isLeap(year))).toEpochDay();
        var randomDay = minDay + random.nextLong() % (maxDay - minDay);

        return LocalDate.ofEpochDay(randomDay).atStartOfDay(ZoneId.systemDefault()).toEpochSecond() * 1000;
    }

}
