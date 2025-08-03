package com.tokioschool.flightapp.flight.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "airports")
public class Airport {

    @Id
    @Column (name = "id")
    private String acronym;

    private String name;
    private String country;

    @Column (name = "latitude")
    private BigDecimal lat;
    @Column (name = "longitude")
    private BigDecimal lon;


}
