package com.santanderuk.demo.testcontainers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    private static final String LOCATION_URL = "/customer/%d";

    private final CustomerRepository customerRepository;

    Controller(final CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @PostMapping(path = "/customer",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Object saveNewUser(@RequestBody final Customer customer) {
        final Long userId = customerRepository.save(customer).getId();

        final MultiValueMap<String, String> headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, String.format(LOCATION_URL, userId));
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }
}
