package com.example.VHire.DTO_Layer.WorkerProfileDto;

import jakarta.persistence.Column;
import lombok.*;

import java.math.BigDecimal;



@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class createWorkerProfileDto {

    @NonNull
    private String skills;
    @NonNull
    private Integer experienceYears;
    @NonNull
    private BigDecimal hourlyRate;
    @NonNull
    private String bio;
}


