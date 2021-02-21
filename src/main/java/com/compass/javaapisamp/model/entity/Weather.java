package com.compass.javaapisamp.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name = "WEATHER")
@NoArgsConstructor
@AllArgsConstructor
@IdClass(WeatherID.class)
public class Weather implements Serializable {
    @Id
    @Column(name = "dat")
    private String date;

    @Column(name = "weather")
    private int weather;

    @ManyToOne
    @JoinColumn(name = "location", referencedColumnName = "id")
    private Location location;

    @Id
    @Column(name = "location_id")
    private int location_id;

    @Column(name = "comment")
    private String comment;
}
