package com.example.VHire.Entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Bookings",
        indexes = {
                @Index(
                        name = "idx_booking_status",
                        columnList = "status"
                ),
                @Index(
                        name = "idx_booking_response_deadline",
                        columnList = "response_deadline"
                )
        })
public class Bookings {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private User company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worker_id", nullable = false)
    private User worker;

    @Column(nullable = false)
    @NonNull
    private LocalDate date;

    @Column(nullable = false)
    @NonNull
    private LocalTime startTime;

    @Column(nullable = false)
    @NonNull
    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NonNull
    private BookingStatus status;

    @Column(nullable = false)
    @NonNull
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime responseDeadline;
}
