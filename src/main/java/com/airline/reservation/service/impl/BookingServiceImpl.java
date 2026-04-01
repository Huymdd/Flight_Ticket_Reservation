package com.airline.reservation.service.impl;

import com.airline.reservation.entity.Booking;
import com.airline.reservation.repository.BookingRepository;
import com.airline.reservation.service.BookingService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    public BookingServiceImpl(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Override
    @Transactional
    public Booking createBooking(Booking booking) {
        booking.setHoldExpiry(LocalDateTime.now().plusMinutes(15));
        return bookingRepository.save(booking);
    }

    @Override
    public Optional<Booking> findById(Long id) {
        return bookingRepository.findById(id);
    }

    @Override
    public List<Booking> findByUserId(Long userId) {
        return bookingRepository.findByUser_UserIdOrderByBookingDateDesc(userId);
    }

    @Override
    public List<Booking> findAll() {
        return bookingRepository.findAll();
    }

    @Override
    @Transactional
    public Booking confirmBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking không tồn tại"));
        booking.setBookingStatus(Booking.BookingStatus.CONFIRMED);
        booking.setPaymentStatus(Booking.PaymentStatus.PAID);
        return bookingRepository.save(booking);
    }

    @Override
    @Transactional
    public Booking cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking không tồn tại"));
        booking.setBookingStatus(Booking.BookingStatus.CANCELLED);
        return bookingRepository.save(booking);
    }

    @Override
    @Scheduled(fixedRate = 60000) // Kiểm tra mỗi 1 phút
    @Transactional
    public void releaseExpiredHolds() {
        List<Booking> expiredBookings = bookingRepository
                .findByBookingStatusAndHoldExpiryBefore(
                        Booking.BookingStatus.PENDING, LocalDateTime.now());
        for (Booking booking : expiredBookings) {
            booking.setBookingStatus(Booking.BookingStatus.CANCELLED);
        }
        bookingRepository.saveAll(expiredBookings);
    }
}
