package com.example.nexign.api.service;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Interface representing a service responsible for generating CDR reports.
 */
public interface CdrService {

    /**
     * Generates a CDR report for the specified date.
     *
     * @param date the date for which the report is generated
     * @return an Optional containing the filename of the generated report,
     *         or an empty Optional if the report generation fails
     */
    Optional<String> generateReport(LocalDate date);

}