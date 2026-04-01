package com.airline.reservation.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "airports")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Airport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long airportId;

    @Column(nullable = false, unique = true, length = 10)
    private String airportCode;

    @Column(nullable = false)
    private String airportName;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String country;

    @OneToMany(mappedBy = "departureAirport")
    private List<Flight> departureFlights;

    @OneToMany(mappedBy = "arrivalAirport")
    private List<Flight> arrivalFlights;
}
