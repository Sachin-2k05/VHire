package com.example.vHire.service;

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

        /* ================= DEFAULT SORT (NO RANDOM DATA) ================= */
        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "name").descending()
        );

        /* ================= BASE QUERY ================= */
        Page<User> workers =
                (city != null && !city.isBlank())
                        ? userRepository.findAllByRoleAndCity(Role.WORKER, city, sortedPageable)
                        : userRepository.findByRole(Role.WORKER, sortedPageable);

        /* ================= MAP + FILTER (SAFE) ================= */
        List<WorkerSearchResponseDto> result =
                workers.getContent().stream()
                        .map(worker -> {

                            WorkerProfile profile =
                                    workerProfileRepository.findByWorker(worker).orElse(null);

                            if (profile == null) return null;

                            /* ---- SKILL FILTER ---- */
                            if (skill != null && !skill.isBlank()) {
                                boolean match =
                                        profile.getSkill().stream()
                                                .anyMatch(s ->
                                                        s.equalsIgnoreCase(skill.trim()));
                                if (!match) return null;
                            }

                            /* ---- EXPERIENCE FILTER ---- */
                            if (minExperienceYears != null &&
                                    profile.getExperienceYears() < minExperienceYears) {
                                return null;
                            }

                            /* ---- RATE FILTER ---- */
                            if (maxHourlyRate != null &&
                                    profile.getHourly_rate().compareTo(maxHourlyRate) > 0) {
                                return null;
                            }

                            /* ---- AVAILABILITY FILTER ---- */
                            boolean available = true;
                            if (date != null && startTime != null && endTime != null) {
                                available =
                                        availabilitySlotRepository
                                                .existsByWorkerAndDateAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
                                                        worker, date, startTime, endTime
                                                );
                            }

                            return mapToDto(worker, profile, available);
                        })
                        .filter(Objects::nonNull)
                        .toList();

        /* ================= RETURN CORRECT PAGE ================= */
        return new PageImpl<>(
                result,
                sortedPageable,
                workers.getTotalElements()
        );
    }

    private WorkerSearchResponseDto mapToDto(
            User worker,
            WorkerProfile profile,
            boolean available
    ) {
        WorkerSearchResponseDto dto = new WorkerSearchResponseDto();
        dto.setWorkerId(worker.getId());
        dto.setName(worker.getName());
        dto.setCity(worker.getCity());
        dto.setSkills(profile.getSkill());
        dto.setExperienceYears(profile.getExperienceYears());
        dto.setHourlyRate(profile.getHourly_rate());
        dto.setAvailable(available);
        return dto;
    }
}
