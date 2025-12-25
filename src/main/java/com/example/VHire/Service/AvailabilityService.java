package com.example.VHire.Service;

import com.example.VHire.Entity.Availability_slot;
import com.example.VHire.Entity.Role;
import com.example.VHire.Entity.User;
import com.example.VHire.Repository.AvailabilitySlotRepository;
import com.example.VHire.Repository.BookingRepository;
import com.example.VHire.Repository.UserRepository;
import com.example.VHire.Repository.WorkerProfileRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


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

    public void CreateAvailabilitySlot( User worker , LocalDate date, LocalTime startTime, LocalTime endTime ) {
if(worker.getRole() != Role.Worker){
        throw new IllegalArgumentException(" only worker can create availability slot");
}

if(!startTime.isBefore(endTime)){
    throw new IllegalArgumentException(" startTime must be before endTime");
}

     if(date.isBefore(LocalDate.now())) {
         throw new IllegalArgumentException(" Cannot create availability slot because date is before now");

     }
     boolean overlapExists = bookingRepository.existsAcceptedOverlap(worker, date, startTime, endTime);
     if(overlapExists){
         throw new IllegalArgumentException(" Availability slot overlapss ");

     }

     Availability_slot slot = new Availability_slot();
     slot.setWorker(worker);
     slot.setDate(date);
     slot.setStartTime(startTime);
     slot.setEndTime(endTime);

     availabilitySlotRepository.save(slot);



    }


    public void removeAvailabilitySlot(Long slotId , User worker){
        Availability_slot slot = availabilitySlotRepository.findById(slotId).orElseThrow(() -> new IllegalArgumentException(" Availability slot not found"));


        if(slot.getWorker().getId() != (worker.getId())){
            throw new IllegalArgumentException("you do not have access to this availability slot");
        }
        availabilitySlotRepository.delete(slot);


    }

    public List<Availability_slot> getAvailabilitySlot(User worker , LocalDate date ){
        if(date.isBefore(LocalDate.now())){
            throw new IllegalArgumentException(" Cannot get availability slot because date is before now");

        }
        return availabilitySlotRepository.findByWorkerAndDateOrderByStartTime(worker, date);
    }



}
