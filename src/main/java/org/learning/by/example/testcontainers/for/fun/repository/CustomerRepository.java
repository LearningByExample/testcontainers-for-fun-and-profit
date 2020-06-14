package com.santanderuk.demo.testcontainers.repository;

import com.santanderuk.demo.testcontainers.model.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long> {
}
