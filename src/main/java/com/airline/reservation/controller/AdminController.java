package com.airline.reservation.controller;

import com.airline.reservation.entity.*;
import com.airline.reservation.repository.*;
import com.airline.reservation.service.BookingService;
import com.airline.reservation.service.FlightService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final FlightService flightService;
    private final AirportRepository airportRepository;
    private final AirplaneRepository airplaneRepository;
    private final SeatClassRepository seatClassRepository;
    private final BookingService bookingService;
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;
    private final ServiceRepository serviceRepository;

    public AdminController(FlightService flightService, AirportRepository airportRepository,
                           AirplaneRepository airplaneRepository, SeatClassRepository seatClassRepository,
                           BookingService bookingService, UserRepository userRepository,
                           PaymentRepository paymentRepository, ServiceRepository serviceRepository) {
        this.flightService = flightService;
        this.airportRepository = airportRepository;
        this.airplaneRepository = airplaneRepository;
        this.seatClassRepository = seatClassRepository;
        this.bookingService = bookingService;
        this.userRepository = userRepository;
        this.paymentRepository = paymentRepository;
        this.serviceRepository = serviceRepository;
    }

    @GetMapping
    public String dashboard(Model model) {
        YearMonth currentMonth = YearMonth.now();
        LocalDateTime start = currentMonth.atDay(1).atStartOfDay();
        LocalDateTime end = currentMonth.atEndOfMonth().atTime(23, 59, 59);

        model.addAttribute("monthlyRevenue", paymentRepository.getRevenueBetween(start, end));
        model.addAttribute("totalBookings", bookingService.findAll().size());
        model.addAttribute("totalUsers", userRepository.findAll().size());
        model.addAttribute("totalFlights", flightService.findAll().size());
        return "admin/dashboard";
    }

    // --- Quản lý Sân bay ---
    @GetMapping("/airports")
    public String listAirports(Model model) {
        model.addAttribute("airports", airportRepository.findAll());
        return "admin/airports";
    }

    @PostMapping("/airports/save")
    public String saveAirport(@ModelAttribute Airport airport, RedirectAttributes ra) {
        airportRepository.save(airport);
        ra.addFlashAttribute("success", "Lưu sân bay thành công!");
        return "redirect:/admin/airports";
    }

    @PostMapping("/airports/{id}/delete")
    public String deleteAirport(@PathVariable Long id, RedirectAttributes ra) {
        airportRepository.deleteById(id);
        ra.addFlashAttribute("success", "Xóa sân bay thành công!");
        return "redirect:/admin/airports";
    }

    // --- Quản lý Máy bay ---
    @GetMapping("/airplanes")
    public String listAirplanes(Model model) {
        model.addAttribute("airplanes", airplaneRepository.findAll());
        return "admin/airplanes";
    }

    @PostMapping("/airplanes/save")
    public String saveAirplane(@ModelAttribute Airplane airplane, RedirectAttributes ra) {
        airplaneRepository.save(airplane);
        ra.addFlashAttribute("success", "Lưu máy bay thành công!");
        return "redirect:/admin/airplanes";
    }

    // --- Quản lý Chuyến bay ---
    @GetMapping("/flights")
    public String listFlights(Model model) {
        model.addAttribute("flights", flightService.findAll());
        model.addAttribute("airports", airportRepository.findAll());
        model.addAttribute("airplanes", airplaneRepository.findAll());
        return "admin/flights";
    }

    @PostMapping("/flights/save")
    public String saveFlight(@RequestParam String flightCode,
                             @RequestParam Long airplaneId,
                             @RequestParam Long departureAirportId,
                             @RequestParam Long arrivalAirportId,
                             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime departureTime,
                             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime arrivalTime,
                             @RequestParam BigDecimal basePrice,
                             @RequestParam Integer availableSeats,
                             RedirectAttributes ra) {
        Flight flight = Flight.builder()
                .flightCode(flightCode)
                .airplane(airplaneRepository.findById(airplaneId).orElseThrow())
                .departureAirport(airportRepository.findById(departureAirportId).orElseThrow())
                .arrivalAirport(airportRepository.findById(arrivalAirportId).orElseThrow())
                .departureTime(departureTime)
                .arrivalTime(arrivalTime)
                .basePrice(basePrice)
                .availableSeats(availableSeats)
                .build();
        flightService.save(flight);
        ra.addFlashAttribute("success", "Thêm chuyến bay thành công!");
        return "redirect:/admin/flights";
    }

    // --- Quản lý Bookings ---
    @GetMapping("/bookings")
    public String listBookings(Model model) {
        model.addAttribute("bookings", bookingService.findAll());
        return "admin/bookings";
    }

    @PostMapping("/bookings/{id}/confirm")
    public String confirmBooking(@PathVariable Long id, RedirectAttributes ra) {
        bookingService.confirmBooking(id);
        ra.addFlashAttribute("success", "Xác nhận booking thành công!");
        return "redirect:/admin/bookings";
    }

    // --- Quản lý Khách hàng ---
    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "admin/users";
    }

    // --- Quản lý Hạng vé ---
    @GetMapping("/seat-classes")
    public String listSeatClasses(Model model) {
        model.addAttribute("seatClasses", seatClassRepository.findAll());
        return "admin/seat-classes";
    }

    @PostMapping("/seat-classes/save")
    public String saveSeatClass(@ModelAttribute SeatClass seatClass, RedirectAttributes ra) {
        seatClassRepository.save(seatClass);
        ra.addFlashAttribute("success", "Lưu hạng vé thành công!");
        return "redirect:/admin/seat-classes";
    }

    // --- Quản lý Dịch vụ ---
    @GetMapping("/services")
    public String listServices(Model model) {
        model.addAttribute("services", serviceRepository.findAll());
        return "admin/services";
    }

    @PostMapping("/services/save")
    public String saveService(@ModelAttribute Service service, RedirectAttributes ra) {
        serviceRepository.save(service);
        ra.addFlashAttribute("success", "Lưu dịch vụ thành công!");
        return "redirect:/admin/services";
    }
}
