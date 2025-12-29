package com.example.VHire.Controller;


import com.example.VHire.DTO_Layer.AvailabilityDto.AvailabilityRequestDto;
import com.example.VHire.DTO_Layer.AvailabilityDto.AvailabilityResponseDto;
import com.example.VHire.Entity.User;
import com.example.VHire.Service.AvailabilityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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

    @DeleteMapping("/{availabilityID}")
    @PreAuthorize("hasRole('WORKER')")
    public ResponseEntity<Void> deleteAvailability(
            @AuthenticationPrincipal User worker,
            @Valid @PathVariable Long availabilityID) {

        availabilityService.removeAvailabilitySlot(availabilityID, worker);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }
    @GetMapping
    @PreAuthorize("hasRole('WORKER')")
    public ResponseEntity<List<AvailabilityResponseDto>> getMyAvailability(
            @AuthenticationPrincipal User worker,

            @RequestParam LocalDate date) {


        return ResponseEntity.ok(
                availabilityService.getAvailability(worker , date)
        );
    }



}
