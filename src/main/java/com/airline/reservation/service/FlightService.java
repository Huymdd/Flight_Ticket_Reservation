package com.airline.reservation.service;

import com.airline.reservation.entity.Flight;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface FlightService {
    List<Flight> searchFlights(Long departureAirportId, Long arrivalAirportId,
                               LocalDate departureDate, Integer passengers);
    Map<LocalDate, BigDecimal> getMinPricesForDateRange(Long departureAirportId, Long arrivalAirportId,
                                                        LocalDate startDate, LocalDate endDate, Integer passengers);
    Optional<Flight> findById(Long id);
    List<Flight> findAll();
    Flight save(Flight flight);
    void deleteById(Long id);
}
