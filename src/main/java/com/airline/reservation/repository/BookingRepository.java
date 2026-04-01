package com.airline.reservation.repository;

import com.airline.reservation.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUser_UserIdOrderByBookingDateDesc(Long userId);

    List<Booking> findByBookingStatusAndHoldExpiryBefore(
            Booking.BookingStatus status, LocalDateTime expiry);
}
