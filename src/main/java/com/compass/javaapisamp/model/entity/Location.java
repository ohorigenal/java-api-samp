package com.compass.javaapisamp.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name = "LOCATION")
@NoArgsConstructor
@AllArgsConstructor
public class Location implements Serializable {
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "city")
    private String city;
}
