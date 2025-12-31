package com.example.VHire.Service;

import com.example.VHire.DTO_Layer.AvailabilityDto.AvailabilityRequestDto;
import com.example.VHire.DTO_Layer.AvailabilityDto.AvailabilityResponseDto;
import com.example.VHire.Entity.Availability_slot;
import com.example.VHire.Entity.User;
import com.example.VHire.Repository.AvailabilitySlotRepository;
import com.example.VHire.Repository.BookingRepository;
import com.example.VHire.Repository.UserRepository;
import com.example.VHire.Repository.WorkerProfileRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;


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



    public void createAvailability(User worker, AvailabilityRequestDto dto) {



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

    public List<AvailabilityResponseDto> getAvailability(User worker ) {



        List<Availability_slot> slots =
                availabilitySlotRepository
                        .findByWorkerOrderByDateAscStartTimeAsc(worker);

        return slots.stream()
                .map(this::mapToAvailabilityResponse)
                .toList();
    }
    private AvailabilityResponseDto mapToAvailabilityResponse(Availability_slot availability) {

        AvailabilityResponseDto dto = new AvailabilityResponseDto();
        dto.setId(availability.getId());
        dto.setWorkerId(availability.getWorker().getId());
        dto.setWorkerName(availability.getWorker().getName());
        dto.setDate(availability.getDate());
        dto.setStartTime(availability.getStartTime());
        dto.setEndTime(availability.getEndTime());

        return dto;
    }


    public List<AvailabilityResponseDto> findAvailabilityByDate(LocalDate date) {

        List<Availability_slot> availabilities =
                availabilitySlotRepository.findByDate(date);

        return availabilities.stream()
                .map(this::mapToAvailabilityResponse)
                .collect(Collectors.toList());
    }
}
