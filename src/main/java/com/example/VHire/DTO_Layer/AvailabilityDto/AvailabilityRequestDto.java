package com.example.vHire.dto_Layer.AvailabilityDto;

import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;


@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AvailabilityRequestDto {


    @NotNull
    private LocalDate date ;
    @NotNull
    private LocalTime startTime ;
    @NotNull
    private LocalTime endTime ;

}
