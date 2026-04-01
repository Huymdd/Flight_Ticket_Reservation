package com.airline.reservation.repository;

import com.airline.reservation.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceRepository extends JpaRepository<Service, Long> {
    List<Service> findByStatus(Service.ServiceStatus status);
}
