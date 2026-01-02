package com.example.vHire.controller;


import com.example.vHire.dto_Layer.UserDto.workerResponsedto;
import com.example.vHire.entity.User;
import com.example.vHire.service.WorkerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/workers")
public class WorkerController {
    private final WorkerService workerService;

    public WorkerController(WorkerService workerService) {
        this.workerService = workerService;
    }

    @GetMapping
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<List<workerResponsedto>> getAvailableWorkers(
            @AuthenticationPrincipal User company,
            @RequestParam LocalDate date
    ) {

        List<workerResponsedto> workers =
                workerService.getAvailableWorkersByCityAndDate(
                        company.getCity(),
                        date
                );

        if (workers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(workers);
    }
}
