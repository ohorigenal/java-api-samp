package com.compass.javaapisamp.repository;

import com.compass.javaapisamp.model.entity.Location;
import com.compass.javaapisamp.model.entity.Weather;
import com.compass.javaapisamp.model.entity.WeatherID;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class WeatherManagerTest {

    @Autowired
    private WeatherManager weatherManager;

    @ParameterizedTest
    @CsvSource({
            "20210101, 1, 1, 東京, comment for test",
            "20210102, 2, 2, 四谷, comment2 for test",
    })
    @Sql(value = "classpath:/sql/weather/insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void findById(String date, int w, int location_id, String city, String comment) {
        Optional<Weather> actual = weatherManager.findById(new WeatherID(date, location_id));
        Weather expected = new Weather(date, w, new Location(location_id, city), location_id, comment);
        assertEquals(expected, actual.get());
    }

    @ParameterizedTest
    @CsvSource({
            "99999999, 1",
            "20210101, 999",
    })
    @Sql(value = "classpath:/sql/weather/insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void findById_Null(String date, int location_id) {
        Optional<Weather> actual = weatherManager.findById(new WeatherID(date, location_id));
        assertTrue(actual.isEmpty());
    }

    @ParameterizedTest
    @CsvSource({
            "20210101, 1, 1, 東京, comment for update test",
            "20220101, 1, 1, , comment for new test",
            "20220102, 1, 1, ダミー, comment for new test and ignore city",
    })
    @Sql(value = "classpath:/sql/weather/insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void save_success(String date, int w, int location_id, String city, String comment) {
        Weather weather = new Weather(date, w, new Location(location_id, city), location_id, comment);
        weatherManager.save(weather);
    }

    @ParameterizedTest
    @CsvSource({
            "20210101, 1, 5, 東京, comment for location_id not exist",
            //"202201010, 1, 1, , comment for invalid date length",
    })
    @Sql(value = "classpath:/sql/weather/insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void save_error(String date, int w, int location_id, String city, String comment) {
        Weather weather = new Weather(date, w, new Location(location_id, city), location_id, comment);
        assertThrows(Exception.class, () -> weatherManager.save(weather));
    }
}
