package com.example.VHire.Repository;

import com.example.VHire.Entity.User;
import com.example.VHire.Entity.WorkerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface WorkerProfileRepository extends JpaRepository<WorkerProfile,Integer> {

    Optional<WorkerProfile> findByUser(User user);

    boolean existsByUser(User user);
}
