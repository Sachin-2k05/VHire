package com.example.vHire.controller;

import com.example.vHire.dto_Layer.CompanyProfileDto.CompanyProfileResponseDto;
import com.example.vHire.dto_Layer.CompanyProfileDto.CreateCompanyProfileDto;
import com.example.vHire.dto_Layer.CompanyProfileDto.UpdateCompanyProfileDto;
import com.example.vHire.security.CustomUserDetail;
import com.example.vHire.service.CompanyProfileService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/company-profiles")
public class CompanyProfileController {

    private final CompanyProfileService service;

    public CompanyProfileController(CompanyProfileService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<CompanyProfileResponseDto> create(
            @AuthenticationPrincipal CustomUserDetail user,
            @Valid @RequestBody CreateCompanyProfileDto dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.createProfile(user.getUser(), dto));
    }

    @PutMapping
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<CompanyProfileResponseDto> update(
            @AuthenticationPrincipal CustomUserDetail user,
            @Valid @RequestBody UpdateCompanyProfileDto dto
    ) {
        return ResponseEntity.ok(
                service.updateProfile(user.getUser(), dto));
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<CompanyProfileResponseDto> me(
            @AuthenticationPrincipal CustomUserDetail user
    ) {
        return ResponseEntity.ok(
                service.getMyProfile(user.getUser()));
    }
}
