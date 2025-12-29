package com.example.VHire.Service;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
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
