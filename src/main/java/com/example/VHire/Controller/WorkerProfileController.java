package com.example.vHire.controller;


import com.example.vHire.dto_Layer.WorkerProfileDto.UpdateWorkerProfileDto;
import com.example.vHire.dto_Layer.WorkerProfileDto.WorkerProfileResponseDto;
import com.example.vHire.dto_Layer.WorkerProfileDto.createWorkerProfileDto;
import com.example.vHire.entity.User;
import com.example.vHire.security.CustomUserDetail;
import com.example.vHire.service.WorkerProfileService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/worker-profiles")
public class WorkerProfileController {


    private final WorkerProfileService workerProfileService;

    public WorkerProfileController(WorkerProfileService workerProfileService) {
        this.workerProfileService = workerProfileService;
    }

    @PostMapping
    @PreAuthorize("hasRole('WORKER')")
    public ResponseEntity<WorkerProfileResponseDto> CreateWorkerProfile(@AuthenticationPrincipal CustomUserDetail customUserDetail,
                                                                        @Valid @RequestBody createWorkerProfileDto Dto) {

        return ResponseEntity.status(HttpStatus.CREATED).body(workerProfileService.createProfile(customUserDetail.getUser(), Dto));

    }

@PutMapping
@PreAuthorize("hasRole('WORKER')")
    public ResponseEntity<WorkerProfileResponseDto> UpdateWorkerProfile(
        @AuthenticationPrincipal CustomUserDetail customUserDetail
        ,@Valid @RequestBody UpdateWorkerProfileDto Dto) {

        return ResponseEntity.ok(workerProfileService.updateProfile(customUserDetail.getUser(), Dto));
    }


    @GetMapping("/{workerId}")
    @PreAuthorize("hasAnyRole('WORKER','COMPANY')")
    public ResponseEntity<WorkerProfileResponseDto> getWorkerProfile(
           @Valid @PathVariable Long workerId) {
       return ResponseEntity.ok(workerProfileService.getProfileByWorkerId(workerId));


    }


    @GetMapping("/me")
    @PreAuthorize("hasRole('WORKER')")
    public ResponseEntity<WorkerProfileResponseDto> getMyProfile(
            @AuthenticationPrincipal CustomUserDetail customUserDetail
    ) {



        return ResponseEntity.ok(
                workerProfileService.getProfileByWorker(customUserDetail.getUser())
        );
    }




}
