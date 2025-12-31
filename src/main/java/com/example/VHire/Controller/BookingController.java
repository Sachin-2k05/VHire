package com.example.VHire.Controller;


import com.example.VHire.DTO_Layer.BookingDto.BookingResponseDto;
import com.example.VHire.DTO_Layer.BookingDto.CreateBookingDto;
import com.example.VHire.DTO_Layer.Common.ApiResponse;
import com.example.VHire.Entity.User;
import com.example.VHire.Repository.AvailabilitySlotRepository;
import com.example.VHire.Service.AvailabilityService;
import com.example.VHire.Service.BookingService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {
    private final AvailabilityService availabilityService;
    private final BookingService bookingService;
    public BookingController(BookingService bookingService,  AvailabilityService availabilityService) {
        this.bookingService = bookingService;
        this.availabilityService = availabilityService;

    }

    @PostMapping
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<BookingResponseDto> CreateBooking(
            @AuthenticationPrincipal User company,
            @Valid @RequestBody CreateBookingDto dto) throws Throwable {



        return ResponseEntity.ok(
                bookingService.createBooking(company, dto)
        );
    }


    @PostMapping("/{BookingID}/accept")
    @PreAuthorize("hasRole('WORKER')")
    public ResponseEntity<BookingResponseDto> acceptBooking(
            @AuthenticationPrincipal User worker,
            @PathVariable("BookingID") Long bookingID) {

        return ResponseEntity.ok(bookingService.acceptBooking(bookingID, worker));
    }



    @PostMapping("/{BookingID}/reject")
    @PreAuthorize("hasRole('WORKER')")
    public ResponseEntity<BookingResponseDto> rejectBooking(
            @AuthenticationPrincipal User worker
            ,@PathVariable("BookingID") Long bookingID) {

        bookingService.rejectBooking(bookingID, worker);
        return ResponseEntity.notFound().build();
    }


    @GetMapping("/id/{BookingID}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BookingResponseDto> getBooking(@PathVariable("BookingID") Long bookingID) {
       return ResponseEntity.ok(bookingService.getBookingById(bookingID));

    }

    @GetMapping("/company")
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<ApiResponse<Page<BookingResponseDto>>> getCompanyBookings(
            @AuthenticationPrincipal User company,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = (Pageable) PageRequest.of(page, size);
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Company bookings fetched",
                        bookingService.getCompanyBookings(company, pageable)
                )
        );
    }

    @GetMapping("/worker")
    @PreAuthorize("hasRole('WORKER')")
    public ResponseEntity<ApiResponse<Page<BookingResponseDto>>> getWorkerBookings(
            @AuthenticationPrincipal User worker,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1") int size
    ) {
        Pageable pageable = (Pageable) PageRequest.of(page, size);
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Worker bookings fetched",
                        bookingService.getWorkerBookings(worker, pageable)
                )
        );
    }





}
