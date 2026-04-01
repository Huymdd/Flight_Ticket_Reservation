package com.airline.reservation.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "booking_services")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class BookingService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingServiceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private BigDecimal totalPrice;
}
