package com.example.vHire.controller;

import com.example.vHire.dto_Layer.UserDto.WorkerSearchResponseDto;
import com.example.vHire.service.WorkerSearchService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalTime;

@RestController
@RequestMapping("/companies/workers")
@PreAuthorize("hasRole('COMPANY')")
public class WorkerSearchController {

    private final WorkerSearchService workerSearchService;

    public WorkerSearchController(WorkerSearchService workerSearchService) {
        this.workerSearchService = workerSearchService;
    }

    @GetMapping("/search")
    public Page<WorkerSearchResponseDto> searchWorkers(
            @RequestParam(required = false) String skill,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime endTime,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return workerSearchService.searchWorkers(
                skill, city, date, startTime, endTime, pageable
        );
    }
}
