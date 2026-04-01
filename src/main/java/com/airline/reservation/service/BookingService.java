package com.airline.reservation.service;

import com.airline.reservation.entity.Booking;

import java.util.List;
import java.util.Optional;

public interface BookingService {
    Booking createBooking(Booking booking);
    Optional<Booking> findById(Long id);
    List<Booking> findByUserId(Long userId);
    List<Booking> findAll();
    Booking confirmBooking(Long bookingId);
    Booking cancelBooking(Long bookingId);
    void releaseExpiredHolds();
}
