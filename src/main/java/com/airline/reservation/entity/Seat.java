package com.airline.reservation.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "seats")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seatId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "airplane_id", nullable = false)
    private Airplane airplane;

    @Column(nullable = false)
    private String seatNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id", nullable = false)
    private SeatClass seatClass;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeatStatus seatStatus;

    @OneToMany(mappedBy = "seat")
    private List<BookingDetail> bookingDetails;

    @PrePersist
    protected void onCreate() {
        if (this.seatStatus == null) this.seatStatus = SeatStatus.AVAILABLE;
    }

    public enum SeatStatus {
        AVAILABLE, HELD, BOOKED
    }
}
