package com.example.nexign.api.repository;

import com.example.nexign.model.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByNumber(Integer number);

    Boolean existsByNumber(Integer number);

    Customer getCustomerByNumber(Integer number);

}
