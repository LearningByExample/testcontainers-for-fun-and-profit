package com.santanderuk.demo.testcontainers;

import com.santanderuk.demo.testcontainers.model.Customer;
import com.santanderuk.demo.testcontainers.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
public class ControllerTest {
    private static final String DATASOURCE_URL_PROPERTY = "spring.datasource.url";
    private static final String DATABASE = "customers";
    private static final String USER = "userTest";
    private static final String PASSWORD = "userTestPwd";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        customerRepository.deleteAll();
    }

    @Container
    private static final PostgreSQLContainer POSTGRES_SQL_CONTAINER = new PostgreSQLContainer()
            .withDatabaseName(DATABASE)
            .withUsername(USER)
            .withPassword(PASSWORD);

    @DynamicPropertySource
    static void postgresSQLProperties(final DynamicPropertyRegistry registry) {
        registry.add(DATASOURCE_URL_PROPERTY, POSTGRES_SQL_CONTAINER::getJdbcUrl);
    }

    @Test
    @DisplayName("The service should response with the customer when the id exists")
    void shouldGetOkWhenUserExists() throws Exception {
        final Customer customer = new Customer("Estefania", "Castro", "more details");
        customerRepository.save(customer);

        mockMvc.perform(get("/customer/{id}", customer.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(customer.getId()))
                .andExpect(jsonPath("$.name").value(customer.getName()))
                .andExpect(jsonPath("$.surName").value(customer.getSurName()))
                .andExpect(jsonPath("$.details").value(customer.getDetails()));
    }

    @Test
    @DisplayName("The service should response with not found when the id does not exists")
    void shouldGetNotFoundWhenUserDoesNotExist() throws Exception {
        mockMvc.perform(get("/customer/{id}", Long.MAX_VALUE)
            .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isNotFound());
    }
}
