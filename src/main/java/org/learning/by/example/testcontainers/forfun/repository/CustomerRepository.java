package org.learning.by.example.testcontainers.forfun.repository;

import org.learning.by.example.testcontainers.forfun.model.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long> {
}
