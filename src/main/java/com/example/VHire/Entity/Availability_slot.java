package com.example.VHire.Entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;


@Getter
@Setter
@Entity
@Table(name = "Availbility_slots")
public class Availability_slot{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonNull
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worker_id", nullable = false)
    @NonNull
    private User worker;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    @NonNull
    private LocalTime startTime;

    @Column(nullable = false)
    @NonNull
    private LocalTime endTime;


}
