package com.compass.javaapisamp.repository;

import com.compass.javaapisamp.model.entity.Location;
import com.compass.javaapisamp.model.exception.APIException;
import com.compass.javaapisamp.model.exception.Errors;
import com.compass.javaapisamp.repository.manager.LocationManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.TransientDataAccessException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class LocationRepository {

    private final LocationManager locationManager;

    @Retryable(
        value = RetryableException.class,
        maxAttemptsExpression = "${api.retry.maxAttempts:2}",
        backoff = @Backoff(delayExpression = "${api.retry.backoffDelay:500}"))
    public List<Location> findAll() {
        try {
            return locationManager.findAll();
        } catch (TransientDataAccessException e) {
            log.warn("find locations retryable exception. " + e.getMessage());
            throw new RetryableException(e);
        } catch (DataAccessException e) {
            log.error("find location data access exception. " + e);
            throw new APIException(Errors.INTERNAL_SERVER_ERROR);
        }
    }

    @Recover
    public List<Location> recoverFindAll(RuntimeException e) {
        if(e instanceof RetryableException) {
            log.error("find location retry exhausted. " + e);
            throw new APIException(Errors.INTERNAL_SERVER_ERROR);
        }
        throw e;
    }
}
