package com.compass.javaapisamp.repository;

import com.compass.javaapisamp.model.entity.Location;
import com.compass.javaapisamp.model.exception.APIException;
import com.compass.javaapisamp.model.exception.Errors;
import com.compass.javaapisamp.repository.manager.LocationManager;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.QueryTimeoutException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class LocationRepositoryTest {

    @MockBean
    private LocationManager locationManager;

    @Autowired
    private LocationRepository locationRepository;

    /*
     * find all
     */
    @Test
    void findAllNormalTest() {
        List<Location> expected = List.of(new Location(1, "新宿"), new Location(2, "中野"));
        Mockito.when(locationManager.findAll()).thenReturn(expected);
        List<Location> actual = locationRepository.findAll();
        assertEquals(expected, actual);
    }

    @Test
    void findAllDataAccessErrorTest() {
        Mockito.when(locationManager.findAll()).thenThrow(DataRetrievalFailureException.class);
        APIException e = assertThrows(APIException.class, () -> locationRepository.findAll());
        assertEquals(Errors.INTERNAL_SERVER_ERROR, e.getError());
    }

    @Test
    void findAllTimeoutRetryErrorTest() {
        Mockito.when(locationManager.findAll()).thenThrow(QueryTimeoutException.class);
        APIException e = assertThrows(APIException.class, () -> locationRepository.findAll());
        assertEquals(Errors.INTERNAL_SERVER_ERROR, e.getError());

        Mockito.verify(locationManager, Mockito.times(2)).findAll();
    }
}
