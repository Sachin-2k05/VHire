package com.example.vHire.service;

import com.example.vHire.dto_Layer.WorkerProfileDto.createWorkerProfileDto;
import com.example.vHire.dto_Layer.WorkerProfileDto.UpdateWorkerProfileDto;
import com.example.vHire.dto_Layer.WorkerProfileDto.WorkerProfileResponseDto;
import com.example.vHire.entity.User;
import com.example.vHire.entity.WorkerProfile;
import com.example.vHire.repository.WorkerProfileRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class WorkerProfileService{

    private final WorkerProfileRepository workerProfileRepository;
    private final UserService userService;

    public WorkerProfileService(
            WorkerProfileRepository workerProfileRepository,
            UserService userService
    ) {
        this.workerProfileRepository = workerProfileRepository;
        this.userService = userService;
    }



    public WorkerProfileResponseDto createProfile(
            User worker,
            createWorkerProfileDto dto) {

        if (workerProfileRepository.existsByworker(worker)) {
            throw new IllegalStateException("Worker profile already exists");
        }


        WorkerProfile profile = new WorkerProfile();
        profile.setWorker(worker);
        profile.getWorker().getName();

        profile.setSkill(dto.getSkills());
        profile.setExperienceYears(dto.getExperienceYears());
        profile.setHourly_rate(dto.getHourlyRate());
        profile.setBio(dto.getBio());
        profile.setActive(false);

        workerProfileRepository.save(profile);

        return mapToResponse(profile);
    }


    public WorkerProfileResponseDto updateProfile(
            User worker,
            UpdateWorkerProfileDto dto) {

        WorkerProfile profile = getProfileEntityByWorker(worker);

        profile.setSkill(dto.getSkills());
        profile.setExperienceYears(dto.getExperienceYears());
        profile.setHourly_rate(dto.getHourlyRate());
        profile.setBio(dto.getBio());

        workerProfileRepository.save(profile);

        return mapToResponse(profile);
    }



    public WorkerProfileResponseDto getProfileByWorkerId(long workerId) {

        User worker = userService.getWorkerEntityById(workerId);
        WorkerProfile profile = getProfileEntityByWorker(worker);

        return mapToResponse(profile);
    }


@Transactional
    public WorkerProfileResponseDto getProfileByWorker(User worker) {

        WorkerProfile profile = getProfileEntityByWorker(worker);
        return mapToResponse(profile);
    }




    public boolean isProfileComplete(User worker) {

        WorkerProfile profile = getProfileEntityByWorker(worker);

        return profile.getSkill() != null &&
                !profile.getSkill().isEmpty() &&
                profile.getHourly_rate() != null &&
                profile.getExperienceYears() != null;
    }

    public WorkerProfile getProfileEntityByWorker(User worker) {
        return workerProfileRepository.findByWorker(worker)
                .orElseThrow(() ->
                        new EntityNotFoundException("Worker profile not found"));
    }

    private WorkerProfileResponseDto mapToResponse(WorkerProfile profile) {

        WorkerProfileResponseDto dto = new WorkerProfileResponseDto();
        dto.setWorkerId((long) profile.getWorker().getId());
        dto.setWorkerName(profile.getWorker().getName());
        dto.setSkills(profile.getSkill());
        dto.setExperienceYears(profile.getExperienceYears());
        dto.setHourlyRate(profile.getHourly_rate());
        dto.setBio(profile.getBio());
        dto.setActive(profile.isActive());

        return dto;
    }
}