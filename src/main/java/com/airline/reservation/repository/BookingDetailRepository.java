package com.airline.reservation.repository;

import com.airline.reservation.entity.BookingDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingDetailRepository extends JpaRepository<BookingDetail, Long> {
    List<BookingDetail> findByBooking_BookingId(Long bookingId);
}
