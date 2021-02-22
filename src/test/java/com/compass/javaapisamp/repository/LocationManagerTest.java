package com.compass.javaapisamp.repository;

import com.compass.javaapisamp.model.entity.Location;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class LocationManagerTest {

    @Autowired
    private LocationManager locationManager;


    @ParameterizedTest
    @CsvSource({
            "1, 東京",
            "2, 四谷",
    })
    @Sql(value = "classpath:/sql/location/insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void findById(int id, String city) {
        Optional<Location> actual = locationManager.findById(id);
        assertEquals(new Location(id, city), actual.get());
    }

    @Test
    @Sql(value = "classpath:/sql/location/insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void findById_NotFound() {
        Optional<Location> actual = locationManager.findById(999);
        assertTrue(actual.isEmpty());
    }

}
