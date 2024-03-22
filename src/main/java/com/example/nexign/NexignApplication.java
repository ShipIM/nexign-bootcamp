package com.example.nexign;

import com.example.nexign.api.service.UdrService;
import com.example.nexign.interaction.Commutator;
import org.springframework.beans.BeansException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class NexignApplication implements CommandLineRunner, ApplicationContextAware {

    private ApplicationContext context;

    public static void main(String[] args) {
        SpringApplication.run(NexignApplication.class, args);
    }

    @Override
    public void run(String... args) {
            Commutator commutator = context.getBean(Commutator.class);
            commutator.commit();

            UdrService udrService = context.getBean(UdrService.class);
            udrService.generateReport();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

}
