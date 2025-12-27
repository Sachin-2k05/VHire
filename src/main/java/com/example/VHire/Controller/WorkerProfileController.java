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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/worker-profiles")
public class WorkerProfileController {


    private final WorkerProfileService workerProfileService;

    public WorkerProfileController(WorkerProfileService workerProfileService) {
        this.workerProfileService = workerProfileService;
    }

    @PostMapping
    public ResponseEntity<WorkerProfileResponseDto> CreateWorkerProfile(@Valid @RequestBody createWorkerProfileDto Dto) {
        User currentuser = getCurrentUser();
        return ResponseEntity.status(HttpStatus.CREATED).body(workerProfileService.createProfile(currentuser, Dto));

    }

@PutMapping
    public ResponseEntity<WorkerProfileResponseDto> UpdateWorkerProfile(@Valid @RequestBody UpdateWorkerProfileDto Dto) {
        User currentuser = getCurrentUser();
        return ResponseEntity.ok(workerProfileService.updateProfile(currentuser, Dto));
    }


    @GetMapping("/workerId")
    public ResponseEntity<WorkerProfileResponseDto> getWorkerProfile(@Valid @PathVariable Long workerId) {
       return ResponseEntity.ok(workerProfileService.getProfileByWorkerId(workerId));


    }


    public User getCurrentUser() {
        throw new UnsupportedOperationException("Not supported yet.");

    }
}
