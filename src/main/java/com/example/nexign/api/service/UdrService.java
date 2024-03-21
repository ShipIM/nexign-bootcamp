package com.example.nexign.api.service;

/**
 * Interface representing a service responsible for generating UDR reports.
 */
public interface UdrService {

    /**
     * Generates a UDR report for all customers.
     */
    void generateReport();

    /**
     * Generates a UDR report for a specific customer identified by their MSISDN.
     *
     * @param msisdn the MSISDN of the customer
     */
    void generateReport(Integer msisdn);

    /**
     * Generates a UDR report for a specific customer identified by their MSISDN and a specific month.
     *
     * @param msisdn the MSISDN of the customer
     * @param month  the month for which the report is generated
     */
    void generateReport(Integer msisdn, Integer month);

}
