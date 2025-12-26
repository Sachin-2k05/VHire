package com.example.VHire.DTO_Layer.AvailabilityDto;

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

private LocalDate date;
private LocalTime startTime;
private LocalTime endTime;

}
