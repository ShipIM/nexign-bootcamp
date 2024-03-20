package com.example.nexign.api.service;

/**
 * Interface represents a service responsible for generating CDR (Call Data Record) reports.
 */
public interface CdrService {

    /**
     * Generates a CDR report for the specified year and month.
     *
     * @param year  the year for which the report is generated
     * @param month the month for which the report is generated
     * @return the filename of the generated report
     */
    String generateReport(Integer year, Integer month);

}
