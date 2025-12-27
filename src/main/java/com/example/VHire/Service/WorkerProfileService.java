package com.example.VHire.Service;

import com.example.VHire.DTO_Layer.WorkerProfileDto.createWorkerProfileDto;
import com.example.VHire.DTO_Layer.WorkerProfileDto.UpdateWorkerProfileDto;
import com.example.VHire.DTO_Layer.WorkerProfileDto.WorkerProfileResponseDto;
import com.example.VHire.DTO_Layer.WorkerProfileDto.createWorkerProfileDto;
import com.example.VHire.Entity.Role;
import com.example.VHire.Entity.User;
import com.example.VHire.Entity.WorkerProfile;
import com.example.VHire.Repository.WorkerProfileRepository;
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



    public WorkerProfileResponseDto createProfile(User worker, createWorkerProfileDto dto) {

        userService.validateRole(worker, Role.Worker);

        if (workerProfileRepository.existsByUser(worker)) {
            throw new IllegalStateException("Worker profile already exists");
        }

        WorkerProfile profile = new WorkerProfile();
        profile.setUser(worker);
        profile.setSkill(dto.getSkills());
        profile.setExperienceYears(dto.getExperienceYears());
        profile.setHourly_rate(dto.getHourlyRate());
        profile.setBio(dto.getBio());
        profile.setActive(false);

        workerProfileRepository.save(profile);

        return mapToResponse(profile);
    }


    public WorkerProfileResponseDto updateProfile(User worker,      UpdateWorkerProfileDto dto) {

        userService.validateRole(worker, Role.Worker);

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


    public WorkerProfileResponseDto getProfileByWorker(User worker) {

        userService.validateRole(worker, Role.Worker);
        WorkerProfile profile = getProfileEntityByWorker(worker);

        return mapToResponse(profile);
    }



    public boolean isProfileComplete(User worker) {

        WorkerProfile profile = getProfileEntityByWorker(worker);

        return profile.getSkill() != null &&
                !profile.getSkill().isBlank() &&
                profile.getHourly_rate() != null &&
                profile.getExperienceYears() != null;
    }

    private WorkerProfile getProfileEntityByWorker(User worker) {
        return workerProfileRepository.findByUser(worker)
                .orElseThrow(() ->
                        new EntityNotFoundException("Worker profile not found"));
    }

    private WorkerProfileResponseDto mapToResponse(WorkerProfile profile) {

        WorkerProfileResponseDto dto = new WorkerProfileResponseDto();
        dto.setWorkerId((long) profile.getUser().getId());
        dto.setWorkerName(profile.getUser().getName());
        dto.setSkills(profile.getSkill());
        dto.setExperienceYears(profile.getExperienceYears());
        dto.setHourlyRate(profile.getHourly_rate());
        dto.setBio(profile.getBio());
        dto.setActive(profile.isActive());

        return dto;
    }
}