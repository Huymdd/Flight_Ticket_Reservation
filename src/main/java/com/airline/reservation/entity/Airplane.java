package com.airline.reservation.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "airplanes")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Airplane {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long airplaneId;

    @Column(nullable = false)
    private String airplaneName;

    @Column(nullable = false)
    private String airplaneModel;

    @Column(nullable = false)
    private Integer totalSeats;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AirplaneStatus status;

    @OneToMany(mappedBy = "airplane", cascade = CascadeType.ALL)
    private List<Seat> seats;

    @OneToMany(mappedBy = "airplane")
    private List<Flight> flights;

    @PrePersist
    protected void onCreate() {
        if (this.status == null) this.status = AirplaneStatus.ACTIVE;
    }

    public enum AirplaneStatus {
        ACTIVE, MAINTENANCE, RETIRED
    }
}
