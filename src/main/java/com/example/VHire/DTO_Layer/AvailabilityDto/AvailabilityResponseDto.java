package com.example.vHire.dto_Layer.AvailabilityDto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;


@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AvailabilityResponseDto {

private Long  id  ;
    private Long workerId;
    private String workerName;
private LocalDate date;
private LocalTime startTime;
private LocalTime endTime;

}
