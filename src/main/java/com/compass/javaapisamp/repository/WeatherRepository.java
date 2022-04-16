package com.compass.javaapisamp.repository;

import com.compass.javaapisamp.model.entity.Weather;
import com.compass.javaapisamp.model.entity.WeatherID;
import com.compass.javaapisamp.model.exception.APIException;
import com.compass.javaapisamp.model.exception.Errors;
import com.compass.javaapisamp.repository.manager.WeatherManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.TransientDataAccessException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Repository;

import java.util.NoSuchElementException;

@Slf4j
@Repository
@RequiredArgsConstructor
public class WeatherRepository {

    private final WeatherManager weatherManager;

    @Retryable(
        value = RetryableException.class,
        maxAttemptsExpression = "${api.retry.maxAttempts}",
        backoff = @Backoff(delayExpression = "${api.retry.backoffDelay}"))
    public Weather findById(WeatherID id) {
        try {
            return weatherManager.findById(id).orElseThrow();
        } catch (TransientDataAccessException e) {
            log.warn("find weather retryable exception. " + e.getMessage());
            throw new RetryableException(e);
        } catch (DataAccessException e) {
            log.error("find weather data access exception. " + e);
            throw new APIException(Errors.INTERNAL_SERVER_ERROR);
        } catch (NoSuchElementException e) {
            log.error("find weather not found exception. " + e);
            throw new APIException(Errors.WEATHER_NOT_FOUND);
        }
    }

    @Recover
    public Weather recoverFindById(RuntimeException e, WeatherID id) {
        if(e instanceof RetryableException) {
            log.error("find retry exhausted. " + e);
            throw new APIException(Errors.INTERNAL_SERVER_ERROR);
        }
        throw e;
    }

    @Retryable(
        value = RetryableException.class,
        maxAttemptsExpression = "${api.retry.maxAttempts}",
        backoff = @Backoff(delayExpression = "${api.retry.backoffDelay}"))
    public void saveWeather(Weather weather) {
        try {
            weatherManager.save(weather);
        } catch (TransientDataAccessException e) {
            log.warn("save weather retryable exception. " + e.getMessage());
            throw new RetryableException(e);
        } catch (DataAccessException e) {
            log.error("save weather data access exception. " + e);
            throw new APIException(Errors.INTERNAL_SERVER_ERROR);
        }
    }

    @Recover
    public void recoverSaveWeather(RuntimeException e, Weather weather) {
        if(e instanceof RetryableException) {
            log.error("save retry exhausted. " + e);
            throw new APIException(Errors.INTERNAL_SERVER_ERROR);
        }
        throw e;
    }
}
