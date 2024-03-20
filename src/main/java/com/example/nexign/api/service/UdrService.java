package com.example.nexign.api.service;

public interface UdrService {

    void generateReport();

    void generateReport(Integer msisdn);

    void generateReport(Integer msisdn, Integer month);

}
