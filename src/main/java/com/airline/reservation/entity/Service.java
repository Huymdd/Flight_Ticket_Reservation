package com.airline.reservation.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "services")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long serviceId;

    @Column(nullable = false)
    private String serviceName;

    @Column(nullable = false)
    private BigDecimal price;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ServiceStatus status;

    @OneToMany(mappedBy = "service")
    private List<BookingService> bookingServices;

    @PrePersist
    protected void onCreate() {
        if (this.status == null) this.status = ServiceStatus.ACTIVE;
    }

    public enum ServiceStatus {
        ACTIVE, INACTIVE
    }
}
