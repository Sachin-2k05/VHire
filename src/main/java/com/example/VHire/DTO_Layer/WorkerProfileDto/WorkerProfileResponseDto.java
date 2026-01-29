package com.example.vHire.dto_Layer.WorkerProfileDto;

import com.example.vHire.dto_Layer.AvailabilityDto.AvailabilityRequestDto;
import com.example.vHire.dto_Layer.AvailabilityDto.AvailabilityResponseDto;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkerProfileResponseDto {

    private Long workerId;
    private String workerName;
    private Set<String> skills;
    private Integer experienceYears;
    private BigDecimal hourlyRate;
    private String bio;
    private boolean active;
    private String city ;
    private String email;
    private List<AvailabilityRequestDto> availabilities;

}

