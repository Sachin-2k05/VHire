package com.example.vHire.controller;


import com.example.vHire.dto_Layer.BookingDto.BookingResponseDto;
import com.example.vHire.dto_Layer.BookingDto.CreateBookingDto;
import com.example.vHire.dto_Layer.Common.ApiResponse;
import com.example.vHire.entity.User;
import com.example.vHire.security.CustomUserDetail;
import com.example.vHire.security.CustomUserDetailService;
import com.example.vHire.service.AvailabilityService;
import com.example.vHire.service.BookingService;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {
    private final AvailabilityService availabilityService;
    private final BookingService bookingService;

    public BookingController(BookingService bookingService, AvailabilityService availabilityService) {
        this.bookingService = bookingService;
        this.availabilityService = availabilityService;

    }

    @PostMapping
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<ApiResponse<BookingResponseDto>> CreateBooking(
            @AuthenticationPrincipal CustomUserDetail customUserDetail,
            @Valid @RequestBody CreateBookingDto dto) throws Throwable {


        return ResponseEntity.ok(
                ApiResponse.success(
                        "booking created successfully",
                bookingService.createBooking(customUserDetail.getUser(), dto))
        );
    }


    @PostMapping("/{BookingID}/accept")
    @PreAuthorize("hasRole('WORKER')")
    public ResponseEntity<BookingResponseDto> acceptBooking(
            @AuthenticationPrincipal CustomUserDetail customUserDetail,
            @PathVariable("BookingID") Long bookingID) {

        return ResponseEntity.ok(bookingService.acceptBooking(bookingID, customUserDetail.getUser()));
    }


    @PostMapping("/{BookingID}/reject")
    @PreAuthorize("hasRole('WORKER')")
    public ResponseEntity<BookingResponseDto> rejectBooking(
            @AuthenticationPrincipal CustomUserDetail customUserDetail
            , @PathVariable("BookingID") Long bookingID) {

        bookingService.rejectBooking(bookingID, customUserDetail.getUser());
        return ResponseEntity.ok(
                bookingService.rejectBooking(bookingID, customUserDetail.getUser())
        );
    }


    @GetMapping("/{BookingID}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BookingResponseDto> getBooking(@PathVariable("BookingID") Long bookingID) {
        return ResponseEntity.ok(bookingService.getBookingById(bookingID));

    }

    @GetMapping("/company")
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<ApiResponse<Page<BookingResponseDto>>> getCompanyBookings(
            @AuthenticationPrincipal CustomUserDetail customUserDetail,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = (Pageable) PageRequest.of(page, size);
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Company bookings fetched",
                        bookingService.getCompanyBookings(customUserDetail.getUser(), pageable)
                )
        );
    }

    @GetMapping("/worker")
    @PreAuthorize("hasRole('WORKER')")
    public ResponseEntity<ApiResponse<Page<BookingResponseDto>>> getWorkerBookings(
            @AuthenticationPrincipal CustomUserDetail customUserDetail,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1") int size
    ) {
        Pageable pageable = (Pageable) PageRequest.of(page, size);
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Worker bookings fetched",
                        bookingService.getWorkerBookings(customUserDetail.getUser(), pageable)
                )
        );
    }

    @PutMapping("/{bookingId}/cancel")
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<BookingResponseDto> cancelBooking(
            @PathVariable Long bookingId,
            @AuthenticationPrincipal CustomUserDetail customUserDetail
    ) {
        BookingResponseDto response =
                bookingService.cancelBooking(bookingId, customUserDetail.getUser());

        return ResponseEntity.ok(response);
    }




    }

