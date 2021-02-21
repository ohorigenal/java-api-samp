package com.compass.javaapisamp.repository;

import com.compass.javaapisamp.model.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationManager extends JpaRepository<Location, Integer> {
}
