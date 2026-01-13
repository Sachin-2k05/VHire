package com.example.vHire.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "company_profiles")
@Getter
@Setter
public class CompanyProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @Column(nullable = false)
    private String companyName;

    private String industry;

    private String description;

    private String website;

    private boolean active = true;
}
