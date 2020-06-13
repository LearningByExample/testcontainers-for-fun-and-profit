package com.santanderuk.demo.testcontainers;

import com.santanderuk.demo.testcontainers.model.CustomerDTO;
import com.santanderuk.demo.testcontainers.repository.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application implements CommandLineRunner {

    final CustomerRepository customerRepository;

    public Application(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        customerRepository.save(new CustomerDTO("Estefania", "Castro", "awesome"));
        customerRepository.save(new CustomerDTO("Juan", "Medina", "sensei"));
    }
}
