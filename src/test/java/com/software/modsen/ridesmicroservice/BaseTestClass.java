package com.software.modsen.ridesmicroservice;

import com.software.modsen.ridesmicroservice.controllers.RideController;
import com.software.modsen.ridesmicroservice.entities.ride.Currency;
import com.software.modsen.ridesmicroservice.entities.ride.RideDto;
import com.software.modsen.ridesmicroservice.entities.ride.RideStatus;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.LocalDateTime;

import static org.testcontainers.containers.wait.strategy.Wait.forListeningPort;

@AutoConfigureMessageVerifier
@Testcontainers
@SpringBootTest(classes = RidesMicroserviceApplication.class)
public class BaseTestClass {
    @Autowired
    private RideController rideController;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Container
    @ServiceConnection
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(
            DockerImageName.parse("postgres:15"))
            .withDatabaseName("cab-aggregator-db")
            .withUsername("postgres")
            .withPassword("98479847")
            .waitingFor(forListeningPort());

    @DynamicPropertySource
    static void configureDatabase(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    static int id = 1;

    @BeforeEach
    void setup() {
        jdbcTemplate.update("INSERT INTO ride " +
                        "(passenger_id, driver_id, from_address, to_address, ride_status, order_date_time,"
                        + " price, currency)"
                        + " VALUES(?, ?, ?, ?, ?, ?, ?, ?)",
                id, id, "Nezavisimosty " + id, "Nezavisimosty " + ++id,
                RideStatus.CREATED.name(), LocalDateTime.of(2021, 10, 10,
                        12, 0, 0), 100,
                Currency.BYN.name());

        RestAssuredMockMvc.standaloneSetup(rideController);
    }
}
