package com.example.VHire.Controller;


import com.example.VHire.DTO_Layer.AvailabilityDto.AvailabilityRequestDto;
import com.example.VHire.DTO_Layer.AvailabilityDto.AvailabilityResponseDto;
import com.example.VHire.Entity.User;
import com.example.VHire.Service.AvailabilityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/availability")
public class AvailabilityController {
    private final AvailabilityService availabilityService;

    public AvailabilityController(AvailabilityService availabilityService) {
        this.availabilityService = availabilityService;
    }

    @PostMapping
    public ResponseEntity<Void> createAvailability( @Valid @RequestBody AvailabilityRequestDto availabilityRequestDto) {
        User currentuser = getCurrentUser();
        availabilityService.createAvailability(currentuser , availabilityRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @DeleteMapping("/{availabilityID}")
    public ResponseEntity<Void> deleteAvailability(@Valid @PathVariable Long availabilityID) {
        User currentuser = getCurrentUser();
        availabilityService.removeAvailabilitySlot(availabilityID, currentuser);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }
    @GetMapping
    public ResponseEntity<List<AvailabilityResponseDto>> getMyAvailability() {

        User currentUser = getCurrentUser();
        return ResponseEntity.ok(
                availabilityService.getAvailability(currentUser)
        );
    }


    private User getCurrentUser() {
        throw new UnsupportedOperationException(
                " "
        );
    }
}
