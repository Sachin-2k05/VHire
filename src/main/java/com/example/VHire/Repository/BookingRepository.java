package com.example.VHire.Repository;

import com.example.VHire.Entity.Bookings;
import com.example.VHire.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

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
          AND b.responseDeadline < :now
    """)
    List<Bookings> findExpiredTRequestedBookings(LocalDateTime now);
}
