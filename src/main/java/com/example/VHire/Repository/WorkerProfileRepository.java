package com.example.VHire.Repository;

import com.example.VHire.Entity.User;
import com.example.VHire.Entity.WorkerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WorkerProfileRepository extends JpaRepository<WorkerProfile,Integer> {

    Optional<WorkerProfile> findByUser(User user);

    boolean existsByUser(User user);
}
