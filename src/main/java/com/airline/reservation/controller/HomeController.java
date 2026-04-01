package com.airline.reservation.controller;

import com.airline.reservation.repository.AirportRepository;
import com.airline.reservation.repository.SeatClassRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final AirportRepository airportRepository;
    private final SeatClassRepository seatClassRepository;

    public HomeController(AirportRepository airportRepository, SeatClassRepository seatClassRepository) {
        this.airportRepository = airportRepository;
        this.seatClassRepository = seatClassRepository;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("airports", airportRepository.findAll());
        model.addAttribute("seatClasses", seatClassRepository.findAll());
        return "index";
    }
}
