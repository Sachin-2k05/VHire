package com.example.vHire.entity;


import jakarta.persistence.*;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WorkerProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    private Long id ;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id" , nullable = false , unique = true)
    @NotNull
    private User worker;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "worker_skills",
            joinColumns = @JoinColumn(name = "worker_profile_id")
    )
    @Column(name = "skill")
    private Set<String> skill ;

    @Column(nullable = false , precision = 8 , scale = 2)
    @NotNull
    private BigDecimal Hourly_rate ;

    @Column(length = 1000)
    @NotNull
    private String bio;

    @NotNull
    private Integer experienceYears;

    @NotNull
    @Column(nullable = false)
    private boolean active = true ;
}

