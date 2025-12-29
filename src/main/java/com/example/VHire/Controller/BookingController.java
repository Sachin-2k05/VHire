package com.example.VHire.Controller;


import com.example.VHire.DTO_Layer.BookingDto.BookingResponseDto;
import com.example.VHire.DTO_Layer.BookingDto.CreateBookingDto;
import com.example.VHire.Entity.User;
import com.example.VHire.Repository.AvailabilitySlotRepository;
import com.example.VHire.Service.AvailabilityService;
import com.example.VHire.Service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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


    @GetMapping("{BookingID}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BookingResponseDto> getBooking(@PathVariable("BookingID") Long bookingID) {
       return ResponseEntity.ok(bookingService.getBookingById(bookingID));

    }





}
