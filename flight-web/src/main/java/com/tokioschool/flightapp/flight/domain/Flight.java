package com.tokioschool.flightapp.flight.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table (name = "flights")
public class Flight {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private LocalDateTime created;

    private String number;

    @ManyToOne
    @JoinColumn (name = "airport_departure_id", nullable = false)
    private Airport departure;

    @ManyToOne
    @JoinColumn (name = "airport_arrival_id", nullable = false)
    private Airport arrival;

    @Column (name = "departure_time")
    private LocalDateTime departureTime;

    @Enumerated(EnumType.STRING)
    @Column (columnDefinition = "varchar")
    private FlightStatus status;

    @Column (nullable = false)
    private int capacity;

    @Column(nullable = false)
    private int occupancy;

    @OneToMany(mappedBy = "flight", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FlightBooking> bookings;

    @OneToOne(mappedBy = "flight", cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "flight_image_id", referencedColumnName = "id")
    private FlightImage image;


}
