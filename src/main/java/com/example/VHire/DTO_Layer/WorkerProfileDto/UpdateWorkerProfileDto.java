package com.example.vHire.dto_Layer.WorkerProfileDto;

import lombok.*;

import java.math.BigDecimal;
import java.util.Set;


@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateWorkerProfileDto {
    @NonNull
    private Set<String> skills;
    @NonNull
    private Integer experienceYears;
    @NonNull
    private BigDecimal hourlyRate;
    @NonNull
    private String bio;
}
