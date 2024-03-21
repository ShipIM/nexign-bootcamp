package com.example.nexign.config.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for the generator.
 * These properties specify the parameters for generating test data.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "generator")
public class GeneratorProperties {

    /**
     * Number of transactions to generate.
     */
    private Integer transactions;

    /**
     * Number of customers to generate.
     */
    private Integer customers;

    /**
     * Year for generating data.
     */
    private Integer year;

    /**
     * Starting month for generating data.
     */
    private Integer monthStart;

    /**
     * Ending month for generating data.
     */
    private Integer monthEnd;

}
