package com.example.VHire.DTO_Layer.WorkerProfileDto;

import lombok.*;

import java.math.BigDecimal;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WorkerProfileResponseDto {

    private Long workerId;

    private String workerName;

    private String skills;

    private Integer experienceYears;

    private BigDecimal hourlyRate;

    private String bio;

    private boolean Active;
}


