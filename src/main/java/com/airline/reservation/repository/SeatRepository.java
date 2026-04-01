package com.airline.reservation.repository;

import com.airline.reservation.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    List<Seat> findByAirplane_AirplaneId(Long airplaneId);

    @Query("SELECT s FROM Seat s WHERE s.airplane.airplaneId = :airplaneId " +
           "AND s.seatId NOT IN (" +
           "  SELECT bd.seat.seatId FROM BookingDetail bd " +
           "  WHERE bd.flight.flightId = :flightId " +
           "  AND bd.booking.bookingStatus != 'CANCELLED'" +
           ")")
    List<Seat> findAvailableSeats(@Param("airplaneId") Long airplaneId,
                                   @Param("flightId") Long flightId);
}
