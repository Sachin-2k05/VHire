package com.example.vHire.service;

import com.example.vHire.dto_Layer.AvailabilityDto.AvailabilityRequestDto;
import com.example.vHire.dto_Layer.AvailabilityDto.AvailabilityResponseDto;
import com.example.vHire.dto_Layer.UserDto.WorkerSearchResponseDto;
import com.example.vHire.entity.Role;
import com.example.vHire.entity.User;
import com.example.vHire.entity.WorkerProfile;
import com.example.vHire.repository.AvailabilitySlotRepository;
import com.example.vHire.repository.UserRepository;
import com.example.vHire.repository.WorkerProfileRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@Service
@Transactional
public class WorkerSearchService {

    private final UserRepository userRepository;
    private final WorkerProfileRepository workerProfileRepository;
    private final AvailabilitySlotRepository availabilitySlotRepository;

    public WorkerSearchService(
            UserRepository userRepository,
            WorkerProfileRepository workerProfileRepository,
            AvailabilitySlotRepository availabilitySlotRepository
    ) {
        this.userRepository = userRepository;
        this.workerProfileRepository = workerProfileRepository;
        this.availabilitySlotRepository = availabilitySlotRepository;
    }

    public Page<WorkerSearchResponseDto> searchWorkers(
            String skill,
            String city,
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime,
            Integer minExperienceYears,
            BigDecimal maxHourlyRate,
            Pageable pageable
    ) {

        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by("worker.name").descending()
        );

        Page<WorkerProfile> profiles =
                workerProfileRepository.searchWorkers(
                        city,
                        skill,
                        minExperienceYears,
                        maxHourlyRate,
                        sortedPageable
                );
        System.out.println("skill = " + skill);
        System.out.println("city = " + city);
        System.out.println("minExp = " + minExperienceYears);
        System.out.println("maxRate = " + maxHourlyRate);

        return profiles.map(profile -> {

            User worker = profile.getWorker();

            boolean available = true;
            if (date != null && startTime != null && endTime != null) {
                available =
                        availabilitySlotRepository
                                .existsByWorkerAndDateAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
                                        worker, date, startTime, endTime
                                );
            }

            return mapToDto(worker, profile, available);
        });

    }

    private WorkerSearchResponseDto mapToDto(User worker, WorkerProfile profile, boolean available) {
        WorkerSearchResponseDto dto = new WorkerSearchResponseDto();

        dto.setWorkerId(worker.getId());
        dto.setName(worker.getName());
        dto.setCity(worker.getCity());
        dto.setSkills(profile.getSkill());
        dto.setExperienceYears(profile.getExperienceYears());
        dto.setHourlyRate(profile.getHourly_rate());
        dto.setAvailable(available);

        if (profile.getAvailabilities() != null) {
            List<AvailabilityResponseDto> availabilityList = profile.getAvailabilities().stream()
                    .map(slot -> {
                        // Use the no-args constructor and setters to avoid parameter mismatch
                        AvailabilityResponseDto res = new AvailabilityResponseDto();
                        res.setId(slot.getId());
                        res.setWorkerId(worker.getId());
                        res.setWorkerName(worker.getName());
                        res.setDate(slot.getDate());
                        res.setStartTime(slot.getStartTime());
                        res.setEndTime(slot.getEndTime());
                        return res;
                    })
                    .toList();

            dto.setAvailabilities(availabilityList);
        }

        return dto;
    }
}
