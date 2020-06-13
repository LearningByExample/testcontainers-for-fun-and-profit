package com.santanderuk.demo.testcontainers;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
public class ControllerTest {

    private static final Customer VALID_BODY = new Customer(
            "Estefania", "Castro", "more details");
    private static final String DATASOURCE_URL_PROPERTY = "spring.datasource.url";
    private static final String DATABASE = "people";
    private static final String USER = "estefania";
    private static final String PASSWORD = "estefaniapwd";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository customerRepository;

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
    void getResponseOK() throws Exception {
        final String body = new Gson().toJson(VALID_BODY);

        final MvcResult mvcResult = mockMvc.perform(post("/customer")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(status().isCreated())
                .andReturn();

        final Long id = Long.valueOf(Objects.requireNonNull(
                mvcResult.getResponse().getHeaderValue(HttpHeaders.LOCATION))
                .toString()
                .split("/")[2]);
        checkDatabaseRecords(id);
    }

    private void checkDatabaseRecords(final Long id) {
        final Optional<Customer> customer = customerRepository.findById(id);
        assertThat(customer).isPresent();
        assertThat(customer.get().getName()).isEqualTo(VALID_BODY.getName());
        assertThat(customer.get().getSurName()).isEqualTo(VALID_BODY.getSurName());
        assertThat(customer.get().getDetails()).isEqualTo(VALID_BODY.getDetails());
    }

    @Test
    void getBadRequestWhenBodyIsNotSend() throws Exception {
        mockMvc.perform(post("/customer")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().doesNotExist(HttpHeaders.LOCATION))
                .andExpect(status().isBadRequest());
    }
}
