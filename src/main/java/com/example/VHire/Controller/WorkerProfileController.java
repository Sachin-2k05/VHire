package com.example.VHire.Controller;


import com.example.VHire.DTO_Layer.WorkerProfileDto.UpdateWorkerProfileDto;
import com.example.VHire.DTO_Layer.WorkerProfileDto.WorkerProfileResponseDto;
import com.example.VHire.DTO_Layer.WorkerProfileDto.createWorkerProfileDto;
import com.example.VHire.Entity.User;
import com.example.VHire.Service.WorkerProfileService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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
    public ResponseEntity<WorkerProfileResponseDto> CreateWorkerProfile( @AuthenticationPrincipal User worker,
                                                                         @Valid @RequestBody createWorkerProfileDto Dto) {

        return ResponseEntity.status(HttpStatus.CREATED).body(workerProfileService.createProfile(worker, Dto));

    }

@PutMapping
@PreAuthorize("hasRole('WORKER')")
    public ResponseEntity<WorkerProfileResponseDto> UpdateWorkerProfile(
        @AuthenticationPrincipal User worker
        ,@Valid @RequestBody UpdateWorkerProfileDto Dto) {

        return ResponseEntity.ok(workerProfileService.updateProfile(worker, Dto));
    }


    @GetMapping("/workerId")
    @PreAuthorize("hasAnyRole('WORKER','COMPANY')")
    public ResponseEntity<WorkerProfileResponseDto> getWorkerProfile(
           @Valid @PathVariable Long workerId) {
       return ResponseEntity.ok(workerProfileService.getProfileByWorkerId(workerId));


    }


    @GetMapping("/me")
    @PreAuthorize("hasRole('WORKER')")
    public ResponseEntity<WorkerProfileResponseDto> getMyProfile(
            @AuthenticationPrincipal User worker
    ) {



        return ResponseEntity.ok(
                workerProfileService.getProfileByWorker(worker)
        );
    }




}
