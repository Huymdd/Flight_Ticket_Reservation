package com.airline.reservation.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "passengers")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Passenger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long passengerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @Column(nullable = false)
    private String fullName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Column(nullable = false)
    private String nationality;

    @Column(nullable = false)
    private String documentType;

    @Column(nullable = false)
    private String documentNumber;

    private LocalDate documentExpiryDate;

    @OneToMany(mappedBy = "passenger")
    private List<BookingDetail> bookingDetails;

    public enum Gender {
        MALE, FEMALE, OTHER
    }
}
