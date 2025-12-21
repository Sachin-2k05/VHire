package com.example.VHire.Entity;


import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class WorkerProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id ;

    @OneToOne
    @JoinColumn(name = "user_id" , nullable = false , unique = true)
    private User user;

    @Column(nullable = false , length = 100)
    private String skill ;

    @Column(nullable = false , precision = 8 , scale = 2)
    private BigDecimal Hourly_rate ;


    @Column(nullable = false)
    private boolean active = true ;
}

