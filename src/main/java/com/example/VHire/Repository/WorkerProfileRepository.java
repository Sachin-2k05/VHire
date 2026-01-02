package com.example.vHire.repository;

import com.example.vHire.entity.User;
import com.example.vHire.entity.WorkerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface WorkerProfileRepository extends JpaRepository<WorkerProfile,Integer> {

    Optional<WorkerProfile> findByWorker(User worker);

    boolean existsByworker(User worker);


    List<WorkerProfile> findBySkillContainingIgnoreCaseAndActiveTrue(
            String skill
    );
}
