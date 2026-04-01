package com.airline.reservation.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "bookings")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(updatable = false)
    private LocalDateTime bookingDate;

    @Column(nullable = false)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus bookingStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus paymentStatus;

    // Thời gian hết hạn giữ chỗ tạm thời (15 phút)
    private LocalDateTime holdExpiry;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL)
    private List<Passenger> passengers;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL)
    private List<BookingDetail> bookingDetails;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL)
    private List<BookingService> bookingServices;

    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL)
    private Payment payment;

    @PrePersist
    protected void onCreate() {
        this.bookingDate = LocalDateTime.now();
        if (this.bookingStatus == null) this.bookingStatus = BookingStatus.PENDING;
        if (this.paymentStatus == null) this.paymentStatus = PaymentStatus.UNPAID;
        if (this.holdExpiry == null) this.holdExpiry = LocalDateTime.now().plusMinutes(15);
    }

    public enum BookingStatus {
        PENDING, CONFIRMED, CANCELLED, COMPLETED
    }

    public enum PaymentStatus {
        UNPAID, PAID, REFUNDED
    }
}
