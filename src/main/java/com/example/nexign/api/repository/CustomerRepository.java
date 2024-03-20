package com.example.nexign.api.repository;

import com.example.nexign.model.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing Customer entities.
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    /**
     * Retrieves a customer by their number.
     *
     * @param number the number of the customer
     * @return an Optional containing the customer, or empty if not found
     */
    Optional<Customer> findByNumber(Integer number);

    /**
     * Checks if a customer with the given number exists.
     *
     * @param number the number of the customer
     * @return true if a customer with the given number exists, false otherwise
     */
    Boolean existsByNumber(Integer number);

    /**
     * Retrieves a customer by their number.
     *
     * @param number the number of the customer
     * @return the customer with the given number
     */
    Customer getCustomerByNumber(Integer number);

}
