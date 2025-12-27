package com.example.VHire.Controller;


import com.example.VHire.DTO_Layer.BookingDto.BookingResponseDto;
import com.example.VHire.DTO_Layer.BookingDto.CreateBookingDto;
import com.example.VHire.Entity.User;
import com.example.VHire.Service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {
    private final BookingService bookingService;
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;

    }

    @PostMapping
    public ResponseEntity<BookingResponseDto> CreateBooking(
            @Valid @RequestBody CreateBookingDto dto) throws Throwable {

        User currentUser = getCurrentUser();
        return ResponseEntity.ok(
                bookingService.createBooking(currentUser, dto)
        );
    }


    @PostMapping("/{BookingID}/accept")
    public ResponseEntity<BookingResponseDto> acceptBooking(@PathVariable("BookingID") long bookingID) {
        User currentUser = getCurrentUser();
        return ResponseEntity.ok(bookingService.acceptBooking(bookingID, currentUser));
    }



    @PostMapping("/{BookingID}/reject")
    public ResponseEntity<BookingResponseDto> rejectBooking(@PathVariable("BookingID") long bookingID) {
        User currentUser = getCurrentUser();
        bookingService.rejectBooking(bookingID, currentUser);
        return ResponseEntity.notFound().build();
    }


    @GetMapping("{BookingID")
    public ResponseEntity<BookingResponseDto> getBooking(@PathVariable("BookingID") long bookingID) {
       return ResponseEntity.ok(bookingService.getBookingById(bookingID));

    }




    private User getCurrentUser() {
        throw new UnsupportedOperationException(
                "Injected from SecurityContext after Spring Security");
    }
}
