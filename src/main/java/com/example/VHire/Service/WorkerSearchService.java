package com.example.vHire.service;

import com.example.vHire.dto_Layer.UserDto.WorkerSearchResponseDto;
import com.example.vHire.entity.Role;
import com.example.vHire.entity.User;
import com.example.vHire.entity.WorkerProfile;
import com.example.vHire.repository.AvailabilitySlotRepository;
import com.example.vHire.repository.UserRepository;
import com.example.vHire.repository.WorkerProfileRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;


@Service
@Transactional
public class WorkerSearchService {
    private final UserRepository userRepository;
    private final WorkerProfileRepository workerProfileRepository;
    private final AvailabilitySlotRepository availabilitySlotRepository;

    public WorkerSearchService(UserRepository userRepository, WorkerProfileRepository workerProfileRepository, AvailabilitySlotRepository availabilitySlotRepository) {
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
            Pageable pageable
    ) {

        Page<User> workers =
                (city != null && !city.isBlank())
                        ? userRepository.findAllByRoleAndCity(Role.WORKER, city, pageable)
                        : userRepository.findByRole(Role.WORKER, pageable);

        List<WorkerSearchResponseDto> result =
                workers.getContent().stream()
                        .map(worker -> {

                            WorkerProfile profile =
                                    workerProfileRepository.findByWorker(worker)
                                            .orElse(null);

                            if (profile == null) return null;

                            if (skill != null &&
                                    profile.getSkill().stream()
                                            .noneMatch(s -> s.equalsIgnoreCase(skill))) {
                                return null;
                            }

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

        return new PageImpl<>(result, pageable, result.size());


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
        return dto;
    }
}


