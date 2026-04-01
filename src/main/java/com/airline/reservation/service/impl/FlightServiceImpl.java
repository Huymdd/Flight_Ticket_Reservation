package com.airline.reservation.service.impl;

import com.airline.reservation.entity.Flight;
import com.airline.reservation.repository.FlightRepository;
import com.airline.reservation.service.FlightService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class FlightServiceImpl implements FlightService {

    private final FlightRepository flightRepository;

    public FlightServiceImpl(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @Override
    public List<Flight> searchFlights(Long departureAirportId, Long arrivalAirportId,
                                       LocalDate departureDate, Integer passengers) {
        LocalDateTime startOfDay = departureDate.atStartOfDay();
        LocalDateTime endOfDay = departureDate.atTime(23, 59, 59);
        return flightRepository.searchFlights(departureAirportId, arrivalAirportId,
                startOfDay, endOfDay, passengers);
    }

    @Override
    public Map<LocalDate, BigDecimal> getMinPricesForDateRange(Long departureAirportId, Long arrivalAirportId,
                                                                LocalDate startDate, LocalDate endDate, Integer passengers) {
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(23, 59, 59);
        List<Flight> flights = flightRepository.searchFlights(departureAirportId, arrivalAirportId, start, end, passengers);

        Map<LocalDate, BigDecimal> result = new TreeMap<>();
        for (Flight f : flights) {
            LocalDate date = f.getDepartureTime().toLocalDate();
            BigDecimal current = result.get(date);
            if (current == null || f.getBasePrice().compareTo(current) < 0) {
                result.put(date, f.getBasePrice());
            }
        }
        return result;
    }

    @Override
    public Optional<Flight> findById(Long id) {
        return flightRepository.findById(id);
    }

    @Override
    public List<Flight> findAll() {
        return flightRepository.findAll();
    }

    @Override
    public Flight save(Flight flight) {
        return flightRepository.save(flight);
    }

    @Override
    public void deleteById(Long id) {
        flightRepository.deleteById(id);
    }
}
