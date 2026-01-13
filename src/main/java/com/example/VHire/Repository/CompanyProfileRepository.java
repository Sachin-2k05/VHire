package com.example.vHire.repository;

import com.example.vHire.entity.CompanyProfile;
import com.example.vHire.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyProfileRepository
        extends JpaRepository<CompanyProfile, Long> {

    Optional<CompanyProfile> findByUser(User user);

    boolean existsByUser(User user);
}

