package org.learning.by.example.testcontainers.forfun.controller;

import org.learning.by.example.testcontainers.forfun.model.Customer;
import org.learning.by.example.testcontainers.forfun.repository.CustomerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class Controller {

    private final CustomerRepository customerRepository;

    Controller(final CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @GetMapping(path = "/customer/{id}")
    public ResponseEntity<?> getCustomer(@PathVariable final Long id) {
        final Optional<Customer> optionalCustomerDTO = customerRepository.findById(id);
        if (optionalCustomerDTO.isPresent()) {
            return new ResponseEntity<>(optionalCustomerDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
