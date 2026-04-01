package com.airline.reservation.repository;

import com.airline.reservation.entity.BookingService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingServiceRepository extends JpaRepository<BookingService, Long> {
    List<BookingService> findByBooking_BookingId(Long bookingId);
}
