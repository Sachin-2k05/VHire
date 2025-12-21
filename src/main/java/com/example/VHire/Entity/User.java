package com.example.VHire.Entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false,length = 100)
    private String name ;

    @Column(nullable = false, unique = true,length=100)
    private String email ;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
   @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private boolean enabled = true;




}
