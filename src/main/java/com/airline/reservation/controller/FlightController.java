package com.airline.reservation.controller;

import com.airline.reservation.entity.Airport;
import com.airline.reservation.entity.Flight;
import com.airline.reservation.repository.AirportRepository;
import com.airline.reservation.repository.SeatRepository;
import com.airline.reservation.service.FlightService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Controller
@RequestMapping("/flights")
public class FlightController {

    private final FlightService flightService;
    private final AirportRepository airportRepository;
    private final SeatRepository seatRepository;

    public FlightController(FlightService flightService, AirportRepository airportRepository,
                            SeatRepository seatRepository) {
        this.flightService = flightService;
        this.airportRepository = airportRepository;
        this.seatRepository = seatRepository;
    }

    @GetMapping("/search")
    public String searchFlights(
            @RequestParam(required = false) Long departureAirportId,
            @RequestParam(required = false) Long arrivalAirportId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate departureDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate returnDate,
            @RequestParam(defaultValue = "ONE_WAY") String tripType,
            @RequestParam(defaultValue = "1") Integer passengers,
            @RequestParam(required = false) Long selectedOutboundFlightId,
            Model model) {

        model.addAttribute("airports", airportRepository.findAll());
        model.addAttribute("tripType", tripType);

        if (departureAirportId != null && arrivalAirportId != null && departureDate != null) {
            // Save search params for template
            model.addAttribute("departureAirportId", departureAirportId);
            model.addAttribute("arrivalAirportId", arrivalAirportId);
            model.addAttribute("departureDate", departureDate);
            model.addAttribute("returnDate", returnDate);
            model.addAttribute("passengers", passengers);

            // Airport info for display
            Airport depAirport = airportRepository.findById(departureAirportId).orElse(null);
            Airport arrAirport = airportRepository.findById(arrivalAirportId).orElse(null);
            model.addAttribute("depAirport", depAirport);
            model.addAttribute("arrAirport", arrAirport);

            // Outbound flights
            List<Flight> flights = flightService.searchFlights(
                    departureAirportId, arrivalAirportId, departureDate, passengers);
            model.addAttribute("flights", flights);

            // Outbound date bar
            model.addAttribute("outboundDateBar", buildDateBar(
                    departureAirportId, arrivalAirportId, departureDate, passengers));

            // Round-trip: show return flights after outbound is selected
            if ("ROUND_TRIP".equals(tripType) && selectedOutboundFlightId != null && returnDate != null) {
                Flight outboundFlight = flightService.findById(selectedOutboundFlightId).orElse(null);
                model.addAttribute("selectedOutboundFlight", outboundFlight);

                // Return flights (swap airports)
                List<Flight> returnFlights = flightService.searchFlights(
                        arrivalAirportId, departureAirportId, returnDate, passengers);
                model.addAttribute("returnFlights", returnFlights);

                // Return date bar
                model.addAttribute("returnDateBar", buildDateBar(
                        arrivalAirportId, departureAirportId, returnDate, passengers));
            }
        }

        return "flight/search";
    }

    @GetMapping("/{id}")
    public String flightDetail(@PathVariable Long id, Model model) {
        Flight flight = flightService.findById(id)
                .orElseThrow(() -> new RuntimeException("Chuyến bay không tồn tại"));
        model.addAttribute("flight", flight);
        model.addAttribute("availableSeats",
                seatRepository.findAvailableSeats(flight.getAirplane().getAirplaneId(), id));
        return "flight/detail";
    }

    private List<Map<String, Object>> buildDateBar(Long depId, Long arrId,
                                                    LocalDate centerDate, Integer passengers) {
        LocalDate startDate = centerDate.minusDays(3);
        LocalDate endDate = centerDate.plusDays(3);

        Map<LocalDate, BigDecimal> prices = flightService.getMinPricesForDateRange(
                depId, arrId, startDate, endDate, passengers);

        String[] dayNames = {"", "Th 2", "Th 3", "Th 4", "Th 5", "Th 6", "Th 7", "CN"};
        List<Map<String, Object>> dateBar = new ArrayList<>();

        for (int i = -3; i <= 3; i++) {
            LocalDate date = centerDate.plusDays(i);
            BigDecimal minPrice = prices.get(date);

            Map<String, Object> entry = new LinkedHashMap<>();
            entry.put("date", date.toString());
            int dow = date.getDayOfWeek().getValue();
            entry.put("dayOfWeek", dayNames[dow] + ", " + date.getDayOfMonth() + " thg " + date.getMonthValue());
            entry.put("minPrice", minPrice);
            entry.put("priceStr", minPrice != null
                    ? "VND " + String.format("%,d", minPrice.longValue()) : "—");
            entry.put("selected", i == 0);
            entry.put("hasFlights", minPrice != null);
            dateBar.add(entry);
        }

        return dateBar;
    }
}
