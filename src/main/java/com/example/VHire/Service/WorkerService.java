package com.example.vHire.service;


import com.example.vHire.dto_Layer.UserDto.workerResponsedto;
import com.example.vHire.entity.User;
import com.example.vHire.entity.WorkerProfile;
import com.example.vHire.repository.AvailabilitySlotRepository;
import com.example.vHire.repository.WorkerProfileRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class WorkerService {
    private final AvailabilitySlotRepository availabilitySlotRepository;
    private final WorkerProfileRepository workerProfileRepository;
    public WorkerService(AvailabilitySlotRepository availabilitySlotRepository, WorkerProfileRepository workerProfileRepository) {
        this.availabilitySlotRepository = availabilitySlotRepository;
        this.workerProfileRepository = workerProfileRepository;

    }

    public List<workerResponsedto> getAvailableWorkersByCityAndDate(String companycity , LocalDate date) {
        List<User> workers = availabilitySlotRepository.findAvailableWorkersByCityAndDate(companycity , date);

return workers.stream().map(this::mapToWorkerResponseDto).toList();

    }

    private workerResponsedto mapToWorkerResponseDto(User worker) {

        WorkerProfile profile =
                workerProfileRepository.findByWorker(worker)
                        .orElseThrow(() ->
                                new IllegalStateException("Worker profile not found")
                        );

        workerResponsedto dto = new workerResponsedto();

        dto.setName(worker.getName());
        dto.setCity(worker.getCity());
        dto.setSkill(profile.getSkill());
        dto.setExperienceInYears(profile.getExperienceYears());
        dto.setHourlyRate(profile.getHourly_rate());

        return dto;
    }
}
