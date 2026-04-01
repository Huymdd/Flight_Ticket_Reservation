package com.airline.reservation.repository;

import com.airline.reservation.entity.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PassengerRepository extends JpaRepository<Passenger, Long> {
    List<Passenger> findByBooking_BookingId(Long bookingId);
}
