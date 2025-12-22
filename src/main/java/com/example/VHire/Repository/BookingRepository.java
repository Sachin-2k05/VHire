package com.example.VHire.Repository;

import com.example.VHire.Entity.Bookings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Bookings,Integer> {

}
