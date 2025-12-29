package com.example.VHire.Repository;

import com.example.VHire.Entity.Availability_slot;
import com.example.VHire.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


@Repository
public interface AvailabilitySlotRepository extends JpaRepository<Availability_slot,Long> {



    @Query("""
        SELECT a
        FROM Availability_slot a
        WHERE a.worker = :worker
          AND a.date = :date
          AND a.startTime <= :startTime
          AND a.endTime >= :endTime
    """)
    List<Availability_slot> findMatchingAvailability(
            @Param("worker") User worker,
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );

    List<Availability_slot> findByWorkerAndDateOrderByStartTime(User worker  , LocalDate date);
}
