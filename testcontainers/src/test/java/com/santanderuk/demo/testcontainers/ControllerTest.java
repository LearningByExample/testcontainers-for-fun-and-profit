package com.santanderuk.demo.testcontainers;

import com.santanderuk.demo.testcontainers.repository.CustomerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
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
    @DisplayName("The service should response with the customer when the id exists")
    void shouldGetOkWhenUserExists() throws Exception {
        final MvcResult mvcResult = mockMvc.perform(get("/customer/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Estefania"))
                .andExpect(jsonPath("$.surName").value("Castro"))
                .andExpect(jsonPath("$.details").value("awesome"))
                .andReturn();
    }

    @Test
    @DisplayName("The service should response with not found when the id does not exists")
    void shouldGetNotFoundWhenUserDoesNotExist() throws Exception {
        final MvcResult mvcResult = mockMvc.perform(get("/customer/{id}", 10)
            .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isNotFound())
            .andReturn();
    }

    @Test
    @DisplayName("The service should response with bad request when the id param is incorrect")
    void getResponseOKaa() throws Exception {
        final MvcResult mvcResult = mockMvc.perform(get("/customer/{id}", "4a")
            .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isBadRequest())
            .andReturn();
    }
}
