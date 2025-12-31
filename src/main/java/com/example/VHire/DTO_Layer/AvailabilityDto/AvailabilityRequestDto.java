package com.example.VHire.DTO_Layer.AvailabilityDto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;


@Data
@Getter
@Setter

public class AvailabilityRequestDto {


    @NotNull
    private LocalDate date ;
    @NotNull
    private LocalTime startTime ;
    @NotNull
    private LocalTime endTime ;

}
