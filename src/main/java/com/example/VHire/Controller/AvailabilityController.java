package com.example.vHire.controller;


import com.example.vHire.dto_Layer.AvailabilityDto.AvailabilityRequestDto;
import com.example.vHire.dto_Layer.AvailabilityDto.AvailabilityResponseDto;
import com.example.vHire.dto_Layer.Common.ApiResponse;
import com.example.vHire.entity.User;
import com.example.vHire.service.AvailabilityService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/availability")
public class AvailabilityController {
    private final AvailabilityService availabilityService;

    public AvailabilityController(AvailabilityService availabilityService) {
        this.availabilityService = availabilityService;
    }

    @PostMapping
    @PreAuthorize("hasRole('WORKER')")
    public ResponseEntity<Void> createAvailability(@AuthenticationPrincipal User worker, @Valid @RequestBody AvailabilityRequestDto availabilityRequestDto) {


        availabilityService.createAvailability(worker , availabilityRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

//    @PostMapping
//    @PreAuthorize("isAuthenticated()")
//    public ResponseEntity<Void> createAvailability(
//            Authentication authentication) {
//
//        System.out.println(authentication.getAuthorities());
//        return ResponseEntity.ok().build();
//    }


    @DeleteMapping("/{availabilityID}")
    @PreAuthorize("hasRole('WORKER')")
    public ResponseEntity<Void> deleteAvailability(
            @AuthenticationPrincipal User worker,
            @Valid @PathVariable Long availabilityID) {

        availabilityService.removeAvailabilitySlot(availabilityID, worker);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }
    @GetMapping("/me")
    @PreAuthorize("hasRole('WORKER')")
    public ResponseEntity<List<AvailabilityResponseDto>> getMyAvailability(
            @AuthenticationPrincipal User worker) {


        return ResponseEntity.ok(
                availabilityService.getAvailability(worker)
        );
    }
    @GetMapping("/search")
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<ApiResponse<List<AvailabilityResponseDto>>> searchAvailability(
            @RequestParam LocalDate date
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Available workers fetched",
                        availabilityService.findAvailabilityByDate(date)
                )
        );
    }



}
