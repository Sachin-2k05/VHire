package com.example.vHire.repository;

import com.example.vHire.entity.Availability_slot;
import com.example.vHire.entity.Bookings;
import com.example.vHire.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Bookings,Long> {


    @Query("""
        SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END
        FROM Bookings b
        WHERE b.worker = :worker
          AND b.date = :date
          AND b.status = 'ACCEPTED'
          AND b.startTime < :endTime
          AND b.endTime > :startTime
    """)
    boolean existsAcceptedOverlap(User worker, LocalDate date, LocalTime startTime, LocalTime endTime);

    @Query("""
        SELECT b
        FROM Bookings b
        WHERE b.status = 'REQUESTED'
          AND b.responseDeadline < :cutoff
    """)
    List<Bookings> findExpiredRequestedBookings(@Param("cutoff") LocalDateTime cutoff);



        Page<Bookings> findByCompany(User company, Pageable pageable);

        Page<Bookings> findByWorker(User worker, Pageable pageable);


    boolean existsByWorkerAndDateAndStartTimeLessThanAndEndTimeGreaterThan(
            User worker,
            LocalDate bookingDate,
            LocalTime endTime,
            LocalTime startTime
    );
}
