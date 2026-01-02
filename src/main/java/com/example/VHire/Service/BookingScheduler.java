package com.example.vHire.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component

public class BookingScheduler {

    private final BookingService bookingService;

    public BookingScheduler(BookingService bookingService) {
        this.bookingService = bookingService;
    }


    @Scheduled(fixedRate = 300000)
    public void autoRejectExpiredBookings() {
        bookingService.AutoRejectExpiredBookings();
    }
}
