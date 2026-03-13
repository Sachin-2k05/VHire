package com.example.vHire.repository;

import com.example.vHire.entity.User;
import com.example.vHire.entity.WorkerProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
@Repository
public interface WorkerProfileRepository extends JpaRepository<WorkerProfile,Integer> {

    Optional<WorkerProfile> findByWorker(User worker);

    boolean existsByworker(User worker);


    List<WorkerProfile> findBySkillContainingIgnoreCaseAndActiveTrue(
            String skill
    );
    @Query("""
SELECT wp FROM WorkerProfile wp
JOIN wp.worker u
WHERE u.role = 'WORKER'
AND (:city IS NULL OR :city = '' OR LOWER(wp.city) = LOWER(:city))
AND (:skill IS NULL OR :skill = '' OR :skill MEMBER OF wp.skill)
AND (:minExp IS NULL OR wp.experienceYears >= :minExp)
AND (:maxRate IS NULL OR wp.Hourly_rate <= :maxRate)
""")
    Page<WorkerProfile> searchWorkers(
            @Param("city") String city,
            @Param("skill") String skill,
            @Param("minExp") Integer minExp,
            @Param("maxRate") BigDecimal maxRate,
            Pageable pageable
    );
}
//AND wp.active = true