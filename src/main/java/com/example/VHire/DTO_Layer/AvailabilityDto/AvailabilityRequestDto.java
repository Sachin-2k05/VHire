package com.example.VHire.DTO_Layer.AvailabilityDto;

import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public class AvailabilityRequestDto {


    @NotNull
    private LocalDate date ;
    @NotNull
    private LocalTime startTime ;
    @NotNull
    private LocalTime endTime ;

}
