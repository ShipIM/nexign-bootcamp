package com.example.nexign.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CustomerSummary {

    private Integer msisdn;

    private Long incoming;

    private Long outcoming;

}
