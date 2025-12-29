package com.example.VHire.DTO_Layer.BookingDto;

import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateBookingDto {

    @NotNull
    private Long worker_id;
    @NotNull
    private LocalDate date ;
    @NotNull
    private LocalTime startTime;
    @NotNull
    private LocalTime endTime;


}
