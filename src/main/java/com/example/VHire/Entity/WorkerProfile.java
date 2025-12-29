package com.example.VHire.Entity;


import jakarta.persistence.*;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.math.BigDecimal;

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

    @OneToOne
    @JoinColumn(name = "user_id" , nullable = false , unique = true)
    @NotNull
    private User user;

    @Column(nullable = false , length = 100)
    @NotNull
    private String skill ;

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

