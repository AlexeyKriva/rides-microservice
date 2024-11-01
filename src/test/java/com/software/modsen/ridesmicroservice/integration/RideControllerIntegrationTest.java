package com.software.modsen.ridesmicroservice.integration;

import com.software.modsen.ridesmicroservice.RidesMicroserviceApplication;
import com.software.modsen.ridesmicroservice.clients.DriverClient;
import com.software.modsen.ridesmicroservice.clients.PassengerClient;
import com.software.modsen.ridesmicroservice.entities.driver.Driver;
import com.software.modsen.ridesmicroservice.entities.driver.Sex;
import com.software.modsen.ridesmicroservice.entities.driver.car.Car;
import com.software.modsen.ridesmicroservice.entities.driver.car.CarBrand;
import com.software.modsen.ridesmicroservice.entities.driver.car.CarColor;
import com.software.modsen.ridesmicroservice.entities.passenger.Passenger;
import com.software.modsen.ridesmicroservice.entities.ride.Currency;
import com.software.modsen.ridesmicroservice.entities.ride.Ride;
import com.software.modsen.ridesmicroservice.entities.ride.RideStatus;
import com.software.modsen.ridesmicroservice.repositories.RideRepository;
import com.software.modsen.ridesmicroservice.services.RideService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = RidesMicroserviceApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class RideControllerIntegrationTest extends TestconteinersConfig {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @MockBean
    private PassengerClient passengerClient;

    @MockBean
    private DriverClient driverClient;

    @Autowired
    private RideService rideService;

    @Autowired
    private RideRepository rideRepository;

    @AfterEach
    void setDown() {
        rideRepository.deleteAll();
    }

    List<Ride> defaultRides() {
        return List.of(
                Ride.builder()
                        .fromAddress("Nezavisimosty 1")
                        .toAddress("Nezavisimosty 2")
                        .orderDateTime(LocalDateTime.of(2023, 10, 11, 12, 0,
                                0))
                        .rideStatus(RideStatus.CREATED)
                        .price(100F)
                        .currency(Currency.BYN)
                        .build(),
                Ride.builder()
                        .fromAddress("Nezavisimosty 3")
                        .toAddress("Nezavisimosty 4")
                        .orderDateTime(LocalDateTime.of(2022, 10, 11, 12, 0,
                                0))
                        .rideStatus(RideStatus.COMPLETED)
                        .price(100F)
                        .currency(Currency.BYN)
                        .build(),
                Ride.builder()
                        .fromAddress("Nezavisimosty 5")
                        .toAddress("Nezavisimosty 6")
                        .orderDateTime(LocalDateTime.of(2021, 10, 11, 12, 0,
                                0))
                        .rideStatus(RideStatus.ACCEPTED)
                        .price(100F)
                        .currency(Currency.BYN)
                        .build()
        );
    }

    @Test
    @SneakyThrows
    void getAllRidesTest_ReturnsRides() {
        //given
        List<Ride> rides = defaultRides();
        long passengerId = 1;
        long driverId = 1;
        for (Ride ride: rides) {
            jdbcTemplate.update("INSERT INTO ride " +
                            "(passenger_id, driver_id, from_address, to_address, ride_status, order_date_time,"
                            + " price, currency)"
                            + " VALUES(?, ?, ?, ?, ?, ?, ?, ?)",
                    passengerId++, driverId++, ride.getFromAddress(), ride.getToAddress(),
                    ride.getRideStatus().name(), ride.getOrderDateTime(), ride.getPrice(),
                    ride.getCurrency().name());
        }

        MvcResult mvcResult = mockMvc.perform(get("/api/rides?includeCancelledAndCompleted=true")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        //when
        String responseContent = mvcResult.getResponse().getContentAsString();

        //then
        assertAll("Check response content",
                () -> assertTrue(responseContent.contains("Ivan")),
                () -> assertTrue(responseContent.contains("Leonid")),
                () -> assertTrue(responseContent.contains("Kirill")),
                () -> assertTrue(responseContent.contains("Vova")),
                () -> assertTrue(responseContent.contains("Andrei")),
                () -> assertTrue(responseContent.contains("Sergei")),
                () -> assertTrue(responseContent.contains("Nezavisimosty 1")),
                () -> assertTrue(responseContent.contains("Nezavisimosty 2")),
                () -> assertTrue(responseContent.contains("2023,10,11,12,0")),
                () -> assertTrue(responseContent.contains("Nezavisimosty 3")),
                () -> assertTrue(responseContent.contains("Nezavisimosty 4")),
                () -> assertTrue(responseContent.contains("2022,10,11,12,0")),
                () -> assertTrue(responseContent.contains("Nezavisimosty 5")),
                () -> assertTrue(responseContent.contains("Nezavisimosty 6")),
                () -> assertTrue(responseContent.contains("2021,10,11,12,0"))
        );
    }

    @Test
    @SneakyThrows
    void getAllNotCompletedOrCancelledRidesTest_ReturnsValidRides() {
        //given
        List<Ride> rides = defaultRides();
        long passengerId = 1;
        long driverId = 1;
        for (Ride ride: rides) {
            jdbcTemplate.update("INSERT INTO ride " +
                            "(passenger_id, driver_id, from_address, to_address, ride_status, order_date_time,"
                            + " price, currency)"
                            + " VALUES(?, ?, ?, ?, ?, ?, ?, ?)",
                    passengerId++, driverId++, ride.getFromAddress(), ride.getToAddress(),
                    ride.getRideStatus().name(), ride.getOrderDateTime(), ride.getPrice(),
                    ride.getCurrency().name());
        }

        MvcResult mvcResult = mockMvc.perform(get("/api/rides?includeCancelledAndCompleted=false")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        //when
        String responseContent = mvcResult.getResponse().getContentAsString();

        //then
        assertAll("Check response content",
                () -> assertTrue(responseContent.contains("Ivan")),
                () -> assertFalse(responseContent.contains("Leonid")),
                () -> assertTrue(responseContent.contains("Kirill")),
                () -> assertFalse(responseContent.contains("Vova")),
                () -> assertTrue(responseContent.contains("Andrei")),
                () -> assertTrue(responseContent.contains("Sergei")),
                () -> assertTrue(responseContent.contains("Nezavisimosty 1")),
                () -> assertTrue(responseContent.contains("Nezavisimosty 2")),
                () -> assertTrue(responseContent.contains("2023,10,11,12,0")),
                () -> assertFalse(responseContent.contains("Nezavisimosty 3")),
                () -> assertFalse(responseContent.contains("Nezavisimosty 4")),
                () -> assertFalse(responseContent.contains("2022,10,11,12,0")),
                () -> assertTrue(responseContent.contains("Nezavisimosty 5")),
                () -> assertTrue(responseContent.contains("Nezavisimosty 6")),
                () -> assertTrue(responseContent.contains("2021,10,11,12,0"))
        );
    }

    @Test
    @SneakyThrows
    void getRideByIdTest_ReturnsRide() {
        //given
        Ride ride = defaultRides().get(0);
        long passengerId = 1;
        long driverId = 1;
        jdbcTemplate.update("INSERT INTO ride " +
                        "(passenger_id, driver_id, from_address, to_address, ride_status, order_date_time,"
                        + " price, currency)"
                        + " VALUES(?, ?, ?, ?, ?, ?, ?, ?)",
                passengerId, driverId, ride.getFromAddress(), ride.getToAddress(),
                ride.getRideStatus().name(), ride.getOrderDateTime(), ride.getPrice(),
                ride.getCurrency().name());
        Ride rideFromDb = rideService.getAllRides(true).get(0);

        MvcResult mvcResult = mockMvc.perform(get("/api/rides/" + rideFromDb.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        //when
        String responseContent = mvcResult.getResponse().getContentAsString();

        //then
        assertAll("Check response content",
                () -> assertTrue(responseContent.contains("Ivan")),
                () -> assertTrue(responseContent.contains("Kirill")),
                () -> assertTrue(responseContent.contains("Nezavisimosty 1")),
                () -> assertTrue(responseContent.contains("Nezavisimosty 2")),
                () -> assertTrue(responseContent.contains("2023,10,11,12,0"))
        );
    }

    @Test
    @SneakyThrows
    void getAllRidesByPassengerIdTest_ReturnsRide() {
        //given
        List<Ride> rides = defaultRides();
        long passengerId = 1;
        long driverId = 1;
        for (Ride ride: rides) {
            jdbcTemplate.update("INSERT INTO ride " +
                            "(passenger_id, driver_id, from_address, to_address, ride_status, order_date_time,"
                            + " price, currency)"
                            + " VALUES(?, ?, ?, ?, ?, ?, ?, ?)",
                    passengerId++, driverId++, ride.getFromAddress(), ride.getToAddress(),
                    ride.getRideStatus().name(), ride.getOrderDateTime(), ride.getPrice(),
                    ride.getCurrency().name());
        }

        MvcResult mvcResult = mockMvc.perform(get("/api/rides/passengers/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        //when
        String responseContent = mvcResult.getResponse().getContentAsString();

        //then
        assertAll("Check response content",
                () -> assertTrue(responseContent.contains("Leonid")),
                () -> assertTrue(responseContent.contains("Vova")),
                () -> assertTrue(responseContent.contains("Nezavisimosty 3")),
                () -> assertTrue(responseContent.contains("Nezavisimosty 4")),
                () -> assertTrue(responseContent.contains("2022,10,11,12,0"))
        );
    }

    @Test
    @SneakyThrows
    void getAllRidesByDriverIdTest_ReturnsRide() {
        //given
        List<Ride> rides = defaultRides();
        long passengerId = 1;
        long driverId = 1;
        for (Ride ride: rides) {
            jdbcTemplate.update("INSERT INTO ride " +
                            "(passenger_id, driver_id, from_address, to_address, ride_status, order_date_time,"
                            + " price, currency)"
                            + " VALUES(?, ?, ?, ?, ?, ?, ?, ?)",
                    passengerId++, driverId++, ride.getFromAddress(), ride.getToAddress(),
                    ride.getRideStatus().name(), ride.getOrderDateTime(), ride.getPrice(),
                    ride.getCurrency().name());
        }

        MvcResult mvcResult = mockMvc.perform(get("/api/rides/drivers/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        //when
        String responseContent = mvcResult.getResponse().getContentAsString();

        //then
        assertAll("Check response content",
                () -> assertTrue(responseContent.contains("Ivan")),
                () -> assertTrue(responseContent.contains("Kirill")),
                () -> assertTrue(responseContent.contains("Nezavisimosty 1")),
                () -> assertTrue(responseContent.contains("Nezavisimosty 2")),
                () -> assertTrue(responseContent.contains("2023,10,11,12,0"))
        );
    }

    String rideDto = "{"
            + "\"passengerId\": 1,"
            + "\"driverId\": 2,"
            + "\"fromAddress\": \"Nezavisimosty 7\","
            + "\"toAddress\": \"Nezavisimosty 183\","
            + "\"orderDateTime\": \"2022-10-15T12:00:00\","
            + "\"price\": 25,"
            + "\"currency\": \"BYN\""
            + "}";

    @Test
    @SneakyThrows
    void saveRideTest_ReturnsRide() {
        //given
        Passenger mockPassenger = new Passenger();
        mockPassenger.setId(1L);
        mockPassenger.setName("Ivan");
        mockPassenger.setEmail("ivan@gmail.com");
        mockPassenger.setPhoneNumber("+375293578799");
        mockPassenger.setDeleted(false);
        when(passengerClient.getPassengerById(1L))
                .thenReturn(new ResponseEntity<>(mockPassenger, HttpStatus.OK));

        Driver mockDriver = new Driver();
        mockDriver.setId(2L);
        mockDriver.setName("Vova");
        mockDriver.setEmail("vova@gmail.com");
        mockDriver.setPhoneNumber("");
        mockDriver.setSex(Sex.MALE);
        mockDriver.setCar(Car.builder()
                .id(2)
                .color(CarColor.BLACK)
                .brand(CarBrand.BMW)
                .carNumber("3498CD-7")
                .isDeleted(false)
                .build());
        mockDriver.setDeleted(false);
        when(driverClient.getDriverById(2L))
                .thenReturn(new ResponseEntity<>(mockDriver, HttpStatus.OK));

        MvcResult mvcResult = mockMvc.perform(post("/api/rides")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(rideDto))
                .andExpect(status().isOk())
                .andReturn();

        //when
        String responseContent = mvcResult.getResponse().getContentAsString();

        //then
        assertAll("Check response content",
                () -> assertTrue(responseContent.contains("Ivan")),
                () -> assertTrue(responseContent.contains("Vova")),
                () -> assertTrue(responseContent.contains("Nezavisimosty 7")),
                () -> assertTrue(responseContent.contains("Nezavisimosty 183")),
                () -> assertTrue(responseContent.contains("2022,10,15,12,0"))
        );
    }

    String rideUpdateDto = "{"
            + "\"passengerId\": 1,"
            + "\"driverId\": 1,"
            + "\"fromAddress\": \"Nezavisimosty 7\","
            + "\"toAddress\": \"Nezavisimosty 183\","
            + "\"rideStatus\": \"EN_ROUTE_TO_PASSENGER\","
            + "\"orderDateTime\": \"2021-11-18T12:00:00\","
            + "\"price\": 30.5,"
            + "\"currency\": \"BYN\""
            + "}";

    @Test
    @SneakyThrows
    void updateRideByIdTest_ReturnsRide() {
        //given
        Ride ride = defaultRides().get(0);
        long passengerId = 1;
        long driverId = 1;
        jdbcTemplate.update("INSERT INTO ride " +
                        "(passenger_id, driver_id, from_address, to_address, ride_status, order_date_time,"
                        + " price, currency)"
                        + " VALUES(?, ?, ?, ?, ?, ?, ?, ?)",
                passengerId, driverId, ride.getFromAddress(), ride.getToAddress(),
                ride.getRideStatus().name(), ride.getOrderDateTime(), ride.getPrice(),
                ride.getCurrency().name());

        Ride rideFromDb = rideService.getAllRides(true).get(0);

        Passenger mockPassenger = new Passenger();
        mockPassenger.setId(1L);
        mockPassenger.setName("Ivan");
        mockPassenger.setEmail("ivan@gmail.com");
        mockPassenger.setPhoneNumber("+375293578799");
        mockPassenger.setDeleted(false);
        when(passengerClient.getPassengerById(1L))
                .thenReturn(new ResponseEntity<>(mockPassenger, HttpStatus.OK));

        Driver mockDriver = new Driver();
        mockDriver.setId(1L);
        mockDriver.setName("Kirill");
        mockDriver.setEmail("kirill@gmail.com");
        mockDriver.setPhoneNumber("+375298877123");
        mockDriver.setSex(Sex.MALE);
        mockDriver.setCar(Car.builder()
                .id(1)
                .color(CarColor.WHITE)
                .brand(CarBrand.FORD)
                .carNumber("1257AB-1")
                .isDeleted(false)
                .build());
        mockDriver.setDeleted(false);
        when(driverClient.getDriverById(1L))
                .thenReturn(new ResponseEntity<>(mockDriver, HttpStatus.OK));

        MvcResult mvcResult = mockMvc.perform(put("/api/rides/" + rideFromDb.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(rideUpdateDto))
                .andExpect(status().isOk())
                .andReturn();

        //when
        String responseContent = mvcResult.getResponse().getContentAsString();

        //then
        assertAll("Check response content",
                () -> assertTrue(responseContent.contains("Ivan")),
                () -> assertTrue(responseContent.contains("Kirill")),
                () -> assertTrue(responseContent.contains("Nezavisimosty 7")),
                () -> assertTrue(responseContent.contains("Nezavisimosty 183")),
                () -> assertTrue(responseContent.contains("2021,11,18,12,0"))
        );
    }

    String ridePatchDto = "{"
            + "\"fromAddress\": \"Mirnay 17\","
            + "\"toAddress\": \"Armeyskay 3\""
            + "}";

    @Test
    @SneakyThrows
    void patchRideByIdTest_ReturnsRide() {
        //given
        Ride ride = defaultRides().get(0);
        long passengerId = 1;
        long driverId = 1;
        jdbcTemplate.update("INSERT INTO ride " +
                        "(passenger_id, driver_id, from_address, to_address, ride_status, order_date_time,"
                        + " price, currency)"
                        + " VALUES(?, ?, ?, ?, ?, ?, ?, ?)",
                passengerId, driverId, ride.getFromAddress(), ride.getToAddress(),
                ride.getRideStatus().name(), ride.getOrderDateTime(), ride.getPrice(),
                ride.getCurrency().name());

        Ride rideFromDb = rideService.getAllRides(true).get(0);

        Passenger mockPassenger = new Passenger();
        mockPassenger.setId(1L);
        mockPassenger.setName("Ivan");
        mockPassenger.setEmail("ivan@gmail.com");
        mockPassenger.setPhoneNumber("+375293578799");
        mockPassenger.setDeleted(false);
        when(passengerClient.getPassengerById(1L))
                .thenReturn(new ResponseEntity<>(mockPassenger, HttpStatus.OK));

        Driver mockDriver = new Driver();
        mockDriver.setId(1L);
        mockDriver.setName("Kirill");
        mockDriver.setEmail("kirill@gmail.com");
        mockDriver.setPhoneNumber("+375298877123");
        mockDriver.setSex(Sex.MALE);
        mockDriver.setCar(Car.builder()
                .id(1)
                .color(CarColor.WHITE)
                .brand(CarBrand.FORD)
                .carNumber("1257AB-1")
                .isDeleted(false)
                .build());
        mockDriver.setDeleted(false);
        when(driverClient.getDriverById(1L))
                .thenReturn(new ResponseEntity<>(mockDriver, HttpStatus.OK));

        MvcResult mvcResult = mockMvc.perform(patch("/api/rides/" + rideFromDb.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ridePatchDto))
                .andExpect(status().isOk())
                .andReturn();

        //when
        String responseContent = mvcResult.getResponse().getContentAsString();

        //then
        assertAll("Check response content",
                () -> assertTrue(responseContent.contains("Ivan")),
                () -> assertTrue(responseContent.contains("Kirill")),
                () -> assertTrue(responseContent.contains("Mirnay 17")),
                () -> assertTrue(responseContent.contains("Armeyskay 3"))
        );
    }

    @Test
    @SneakyThrows
    void updateRideStatusByIdTest_ReturnsRide() {
        //given
        Ride ride = defaultRides().get(0);
        long passengerId = 1;
        long driverId = 1;
        jdbcTemplate.update("INSERT INTO ride " +
                        "(passenger_id, driver_id, from_address, to_address, ride_status, order_date_time,"
                        + " price, currency)"
                        + " VALUES(?, ?, ?, ?, ?, ?, ?, ?)",
                passengerId, driverId, ride.getFromAddress(), ride.getToAddress(),
                ride.getRideStatus().name(), ride.getOrderDateTime(), ride.getPrice(),
                ride.getCurrency().name());

        Ride rideFromDb = rideService.getAllRides(true).get(0);

        MvcResult mvcResult = mockMvc.perform(patch("/api/rides/" + rideFromDb.getId() +
                        "/status?rideStatus=EN_ROUTE_TO_DESTINATION")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        //when
        String responseContent = mvcResult.getResponse().getContentAsString();

        //then
        assertAll("Check response content",
                () -> assertTrue(responseContent.contains("Ivan")),
                () -> assertTrue(responseContent.contains("Kirill")),
                () -> assertTrue(responseContent.contains("EN_ROUTE_TO_DESTINATION"))
        );
    }

    @Test
    @SneakyThrows
    void deleteRideByIdTest_ReturnsRide() {
        //given
        Ride ride = defaultRides().get(0);
        long passengerId = 1;
        long driverId = 1;
        jdbcTemplate.update("INSERT INTO ride " +
                        "(passenger_id, driver_id, from_address, to_address, ride_status, order_date_time,"
                        + " price, currency)"
                        + " VALUES(?, ?, ?, ?, ?, ?, ?, ?)",
                passengerId, driverId, ride.getFromAddress(), ride.getToAddress(),
                ride.getRideStatus().name(), ride.getOrderDateTime(), ride.getPrice(),
                ride.getCurrency().name());

        Ride rideFromDb = rideService.getAllRides(true).get(0);

        MvcResult mvcResult = mockMvc.perform(delete("/api/rides/" + rideFromDb.getId()))
                .andExpect(status().isOk())
                .andReturn();

        //when
        String responseContent = mvcResult.getResponse().getContentAsString();

        //then
        assertEquals("Ride with id " + rideFromDb.getId() + " was successfully deleted.", responseContent);
    }

    @Test
    @SneakyThrows
    void deleteRideByPassengerIdTest_ReturnsRide() {
        //given
        Ride ride = defaultRides().get(0);
        long passengerId = 2;
        long driverId = 2;
        jdbcTemplate.update("INSERT INTO ride " +
                        "(passenger_id, driver_id, from_address, to_address, ride_status, order_date_time,"
                        + " price, currency)"
                        + " VALUES(?, ?, ?, ?, ?, ?, ?, ?)",
                passengerId, driverId, ride.getFromAddress(), ride.getToAddress(),
                ride.getRideStatus().name(), ride.getOrderDateTime(), ride.getPrice(),
                ride.getCurrency().name());

        MvcResult mvcResult = mockMvc.perform(delete("/api/rides/passengers/2"))
                .andExpect(status().isOk())
                .andReturn();

        //when
        String responseContent = mvcResult.getResponse().getContentAsString();

        //then
        assertEquals("Rides with passenger id 2 was successfully deleted.", responseContent);
    }

    @Test
    @SneakyThrows
    void deleteRideByDriverIdIdTest_ReturnsRide() {
        //given
        Ride ride = defaultRides().get(0);
        long passengerId = 3;
        long driverId = 3;
        jdbcTemplate.update("INSERT INTO ride " +
                        "(passenger_id, driver_id, from_address, to_address, ride_status, order_date_time,"
                        + " price, currency)"
                        + " VALUES(?, ?, ?, ?, ?, ?, ?, ?)",
                passengerId, driverId, ride.getFromAddress(), ride.getToAddress(),
                ride.getRideStatus().name(), ride.getOrderDateTime(), ride.getPrice(),
                ride.getCurrency().name());

        MvcResult mvcResult = mockMvc.perform(delete("/api/rides/drivers/3"))
                .andExpect(status().isOk())
                .andReturn();

        //when
        String responseContent = mvcResult.getResponse().getContentAsString();

        //then
        assertEquals("Rides with driver id 3 was successfully deleted.", responseContent);
    }
}