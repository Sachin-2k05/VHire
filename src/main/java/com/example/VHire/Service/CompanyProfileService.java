package com.example.vHire.service;

import com.example.vHire.dto_Layer.CompanyProfileDto.CompanyProfileResponseDto;
import com.example.vHire.dto_Layer.CompanyProfileDto.CreateCompanyProfileDto;
import com.example.vHire.dto_Layer.CompanyProfileDto.UpdateCompanyProfileDto;
import com.example.vHire.entity.CompanyProfile;
import com.example.vHire.entity.Role;
import com.example.vHire.entity.User;
import com.example.vHire.repository.CompanyProfileRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class CompanyProfileService {
    private final CompanyProfileRepository repository;
    private final UserService userService;

    public CompanyProfileService(
            CompanyProfileRepository repository,
            UserService userService
    ) {
        this.repository = repository;
        this.userService = userService;
    }

    public CompanyProfileResponseDto createProfile(
            User company,
            CreateCompanyProfileDto dto
    ) {
        userService.validateRole(company, Role.COMPANY);

        if (repository.existsByUser(company)) {
            throw new IllegalStateException("Company profile already exists");
        }

        CompanyProfile profile = new CompanyProfile();
        profile.setUser(company);
        profile.setCompanyName(dto.getCompanyName());
        profile.setIndustry(dto.getIndustry());
        profile.setDescription(dto.getDescription());
        profile.setWebsite(dto.getWebsite());

        repository.save(profile);
        return map(profile);
    }

    public CompanyProfileResponseDto updateProfile(
            User company,
            UpdateCompanyProfileDto dto
    ) {
        userService.validateRole(company, Role.COMPANY);

        CompanyProfile profile = repository.findByUser(company)
                .orElseThrow(() ->
                        new EntityNotFoundException("Company profile not found"));

        if (dto.getCompanyName() != null)
            profile.setCompanyName(dto.getCompanyName());

        profile.setIndustry(dto.getIndustry());
        profile.setDescription(dto.getDescription());
        profile.setWebsite(dto.getWebsite());

        repository.save(profile);
        return map(profile);
    }

    public CompanyProfileResponseDto getMyProfile(User company) {
        userService.validateRole(company, Role.COMPANY);

        CompanyProfile profile = repository.findByUser(company)
                .orElseThrow(() ->
                        new EntityNotFoundException("Company profile not found"));

        return map(profile);
    }

    private CompanyProfileResponseDto map(CompanyProfile p) {
        CompanyProfileResponseDto dto = new CompanyProfileResponseDto();
        dto.setCompanyId(p.getUser().getId());
        dto.setCompanyName(p.getCompanyName());
        dto.setIndustry(p.getIndustry());
        dto.setDescription(p.getDescription());
        dto.setWebsite(p.getWebsite());
        dto.setActive(p.isActive());
        return dto;
    }
}
