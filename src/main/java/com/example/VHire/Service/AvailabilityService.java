package com.example.VHire.Service;

import com.example.VHire.Entity.User;
import com.example.VHire.Repository.AvailabilitySlotRepository;
import com.example.VHire.Repository.BookingRepository;
import com.example.VHire.Repository.UserRepository;
import com.example.VHire.Repository.WorkerProfileRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;


@Service
public class AvailabilityService {
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final WorkerProfileRepository workerProfileRepository;
    private final AvailabilitySlotRepository availabilitySlotRepository;
    private final BookingService bookingService;

    public AvailabilityService (UserRepository userRepository,
                                BookingRepository bookingRepository,
                                WorkerProfileRepository workerProfileRepository,
                                AvailabilitySlotRepository availabilitySlotRepository, BookingService bookingService) {
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.workerProfileRepository = workerProfileRepository;
        this.availabilitySlotRepository = availabilitySlotRepository;
        this.bookingService = bookingService;
    }

    public boolean isWorkerAvailable(
            User worker,
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime
    ) {
        return !availabilitySlotRepository
                .findMatchingAvailability(worker, date, startTime, endTime)
                .isEmpty();
    }
}
