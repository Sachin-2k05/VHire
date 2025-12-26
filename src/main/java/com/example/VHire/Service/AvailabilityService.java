package com.example.VHire.Service;

import com.example.VHire.DTO_Layer.AvailabilityDto.AvailabilityRequestDto;
import com.example.VHire.DTO_Layer.AvailabilityDto.AvailabilityResponseDto;
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
import java.util.Date;
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

    public void createAvailability(User worker, AvailabilityRequestDto dto) {

        if (worker.getRole() != Role.Worker) {
            throw new IllegalArgumentException("Only workers can create availability slots");
        }

        LocalDate date = dto.getDate();
        LocalTime startTime = dto.getStartTime();
        LocalTime endTime = dto.getEndTime();

        if (!startTime.isBefore(endTime)) {
            throw new IllegalArgumentException("Start time must be before end time");
        }

        if (date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Cannot create availability slot in the past");
        }

        boolean overlapExists =
                bookingRepository.existsAcceptedOverlap(
                        worker, date, startTime, endTime
                );

        if (overlapExists) {
            throw new IllegalStateException("Availability slot overlaps with an accepted booking");
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

    public List<AvailabilityResponseDto> getAvailability(User worker) {

        if (worker.getRole() != Role.Worker) {
            throw new IllegalArgumentException("Only workers can view availability");
        }

        List<Availability_slot> slots =
                availabilitySlotRepository
                        .findByWorkerAndDateOrderByStartTime(worker);

        return slots.stream()
                .map(this::mapToAvailabilityResponse)
                .toList();
    }
    private AvailabilityResponseDto mapToAvailabilityResponse(Availability_slot slot) {
        AvailabilityResponseDto dto = new AvailabilityResponseDto();
        dto.setId(slot.getId());
        dto.setDate(slot.getDate());
        dto.setStartTime(slot.getStartTime());
        dto.setEndTime(slot.getEndTime());
        return dto;
    }




}
