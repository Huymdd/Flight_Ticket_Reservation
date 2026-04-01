package com.airline.reservation.repository;

import com.airline.reservation.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, Long> {

    @Query("SELECT f FROM Flight f WHERE f.departureAirport.airportId = :depId " +
           "AND f.arrivalAirport.airportId = :arrId " +
           "AND f.departureTime BETWEEN :startTime AND :endTime " +
           "AND f.status = 'SCHEDULED' " +
           "AND f.availableSeats >= :passengers")
    List<Flight> searchFlights(@Param("depId") Long departureAirportId,
                               @Param("arrId") Long arrivalAirportId,
                               @Param("startTime") LocalDateTime startTime,
                               @Param("endTime") LocalDateTime endTime,
                               @Param("passengers") Integer passengers);

    List<Flight> findByDepartureTimeBetween(LocalDateTime start, LocalDateTime end);
}
