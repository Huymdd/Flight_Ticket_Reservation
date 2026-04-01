package com.airline.reservation.repository;

import com.airline.reservation.entity.SeatClass;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatClassRepository extends JpaRepository<SeatClass, Long> {
}
