package com.example.vHire.dto_Layer.AvailabilityDto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;


@Data
@Getter
@Setter
public class AvailabilityResponseDto {

private Long  id  ;
    private Long workerId;
    private String workerName;
private LocalDate date;
private LocalTime startTime;
private LocalTime endTime;

}
