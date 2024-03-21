package com.example.nexign.config.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "generator")
public class GeneratorProperties {

    private Integer transactions;

    private Integer customers;

    private Integer year;

    private Integer monthStart;

    private Integer monthEnd;

}
