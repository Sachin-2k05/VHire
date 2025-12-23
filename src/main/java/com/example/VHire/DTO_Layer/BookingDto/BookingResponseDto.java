package com.example.VHire.DTO_Layer.BookingDto;

import java.time.LocalDate;
import java.time.LocalTime;

public class BookingResponseDto {


    private Long BookingId;

    private String  CompanyName;

    private String WorkerName ;

    private LocalDate date ;

    private LocalTime startTime;

    private LocalTime endTime;

    private String Status ;
}
