package com.example.vHire.dto_Layer.WorkerProfileDto;

import lombok.*;

import java.math.BigDecimal;
import java.util.Set;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WorkerProfileResponseDto {

    private Long workerId;

    private String workerName;

    private Set<String> skills;

    private Integer experienceYears;

    private BigDecimal hourlyRate;

    private String bio;

    private boolean Active;
}


