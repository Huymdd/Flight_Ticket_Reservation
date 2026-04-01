package com.airline.reservation.controller;

import com.airline.reservation.entity.*;
import com.airline.reservation.repository.*;
import com.airline.reservation.service.BookingService;
import com.airline.reservation.service.FlightService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final FlightService flightService;
    private final UserRepository userRepository;
    private final ServiceRepository serviceRepository;
    private final PaymentRepository paymentRepository;

    public BookingController(BookingService bookingService, FlightService flightService,
                             UserRepository userRepository,
                             ServiceRepository serviceRepository,
                             PaymentRepository paymentRepository) {
        this.bookingService = bookingService;
        this.flightService = flightService;
        this.userRepository = userRepository;
        this.serviceRepository = serviceRepository;
        this.paymentRepository = paymentRepository;
    }

    @GetMapping("/new")
    public String newBooking(@RequestParam Long flightId,
                             @RequestParam(required = false) Long returnFlightId,
                             @RequestParam(defaultValue = "1") Integer passengers,
                             Model model) {
        Flight flight = flightService.findById(flightId)
                .orElseThrow(() -> new RuntimeException("Chuyến bay không tồn tại"));

        model.addAttribute("flight", flight);
        model.addAttribute("passengers", passengers);

        if (returnFlightId != null) {
            Flight returnFlight = flightService.findById(returnFlightId)
                    .orElseThrow(() -> new RuntimeException("Chuyến bay về không tồn tại"));
            model.addAttribute("returnFlight", returnFlight);
        }

        model.addAttribute("services",
                serviceRepository.findByStatus(Service.ServiceStatus.ACTIVE));
        return "booking/new";
    }

    @PostMapping("/create")
    public String createBooking(@RequestParam Long flightId,
                                @RequestParam(required = false) Long returnFlightId,
                                @RequestParam List<String> passengerNames,
                                @RequestParam List<String> passengerGenders,
                                @RequestParam List<String> passengerDobs,
                                @RequestParam List<String> passengerNationalities,
                                @RequestParam List<String> passengerDocTypes,
                                @RequestParam List<String> passengerDocNumbers,
                                @AuthenticationPrincipal UserDetails userDetails,
                                RedirectAttributes redirectAttributes) {
        try {
            User user = userRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("User không tồn tại"));

            Flight flight = flightService.findById(flightId)
                    .orElseThrow(() -> new RuntimeException("Chuyến bay không tồn tại"));

            Flight returnFlight = null;
            if (returnFlightId != null) {
                returnFlight = flightService.findById(returnFlightId)
                        .orElseThrow(() -> new RuntimeException("Chuyến bay về không tồn tại"));
            }

            Booking booking = new Booking();
            booking.setUser(user);
            booking.setTotalAmount(BigDecimal.ZERO);

            List<Passenger> passengerList = new ArrayList<>();
            List<BookingDetail> detailList = new ArrayList<>();
            BigDecimal totalAmount = BigDecimal.ZERO;

            for (int i = 0; i < passengerNames.size(); i++) {
                Passenger passenger = Passenger.builder()
                        .booking(booking)
                        .fullName(passengerNames.get(i))
                        .gender(Passenger.Gender.valueOf(passengerGenders.get(i)))
                        .dateOfBirth(java.time.LocalDate.parse(passengerDobs.get(i)))
                        .nationality(passengerNationalities.get(i))
                        .documentType(passengerDocTypes.get(i))
                        .documentNumber(passengerDocNumbers.get(i))
                        .build();
                passengerList.add(passenger);

                // Outbound detail
                BigDecimal ticketPrice = flight.getBasePrice();
                BookingDetail detail = BookingDetail.builder()
                        .booking(booking)
                        .flight(flight)
                        .passenger(passenger)
                        .ticketPrice(ticketPrice)
                        .build();
                detailList.add(detail);
                totalAmount = totalAmount.add(ticketPrice);

                // Return detail (if round-trip)
                if (returnFlight != null) {
                    BigDecimal returnPrice = returnFlight.getBasePrice();
                    BookingDetail returnDetail = BookingDetail.builder()
                            .booking(booking)
                            .flight(returnFlight)
                            .passenger(passenger)
                            .ticketPrice(returnPrice)
                            .build();
                    detailList.add(returnDetail);
                    totalAmount = totalAmount.add(returnPrice);
                }
            }

            booking.setPassengers(passengerList);
            booking.setBookingDetails(detailList);
            booking.setTotalAmount(totalAmount);

            Booking saved = bookingService.createBooking(booking);
            return "redirect:/bookings/" + saved.getBookingId();

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/flights/" + flightId;
        }
    }

    @GetMapping("/{id}")
    public String bookingDetail(@PathVariable Long id, Model model) {
        Booking booking = bookingService.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking không tồn tại"));
        model.addAttribute("booking", booking);
        return "booking/detail";
    }

    @GetMapping("/history")
    public String bookingHistory(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));
        model.addAttribute("bookings", bookingService.findByUserId(user.getUserId()));
        return "booking/history";
    }

    @GetMapping("/{id}/payment")
    public String paymentPage(@PathVariable Long id, Model model) {
        Booking booking = bookingService.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking không tồn tại"));
        if (booking.getPaymentStatus() == Booking.PaymentStatus.PAID) {
            return "redirect:/bookings/" + id;
        }
        model.addAttribute("booking", booking);
        return "booking/payment";
    }

    @PostMapping("/{id}/payment")
    public String processPayment(@PathVariable Long id,
                                 @RequestParam String paymentMethod,
                                 @RequestParam(required = false) String cardNumber,
                                 @RequestParam(required = false) String cardHolder,
                                 @RequestParam(required = false) String cardExpiry,
                                 @RequestParam(required = false) String cardCvv,
                                 @RequestParam(required = false) String momoPhone,
                                 RedirectAttributes redirectAttributes) {
        try {
            Booking booking = bookingService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Booking không tồn tại"));

            if (booking.getPaymentStatus() == Booking.PaymentStatus.PAID) {
                redirectAttributes.addFlashAttribute("error", "Đơn đặt vé này đã được thanh toán!");
                return "redirect:/bookings/" + id;
            }

            // Tạo mã giao dịch
            String transactionCode = "TXN" + System.currentTimeMillis();

            Payment payment = Payment.builder()
                    .booking(booking)
                    .paymentMethod(Payment.PaymentMethod.valueOf(paymentMethod))
                    .paymentDate(java.time.LocalDateTime.now())
                    .amount(booking.getTotalAmount())
                    .paymentStatus(Payment.PaymentStatus.SUCCESS)
                    .transactionCode(transactionCode)
                    .build();

            paymentRepository.save(payment);

            // Cập nhật trạng thái booking
            booking.setPaymentStatus(Booking.PaymentStatus.PAID);
            booking.setBookingStatus(Booking.BookingStatus.CONFIRMED);
            bookingService.confirmBooking(id);

            redirectAttributes.addFlashAttribute("success", "Thanh toán thành công! Mã giao dịch: " + transactionCode);
            return "redirect:/bookings/" + id;

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Thanh toán thất bại: " + e.getMessage());
            return "redirect:/bookings/" + id + "/payment";
        }
    }

    @PostMapping("/{id}/cancel")
    public String cancelBooking(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            bookingService.cancelBooking(id);
            redirectAttributes.addFlashAttribute("success", "Hủy vé thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/bookings/" + id;
    }
}
