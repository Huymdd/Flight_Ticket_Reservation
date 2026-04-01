package com.airline.reservation.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "seat_classes")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class SeatClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long classId;

    @Column(nullable = false, unique = true)
    private String className;

    private String description;

    @Column(nullable = false)
    private BigDecimal extraPrice;

    @OneToMany(mappedBy = "seatClass")
    private List<Seat> seats;
}
