package com.example.vHire.dto_Layer.UserDto;


import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.Set;

@Data
@Getter
@Setter
@AllArgsConstructor
public class workerResponsedto {

    @NotNull
    private String name;
    @NotNull
    private String role;
    @NotNull
    private String city;
    @NotNull
    private Set<String> skill;
    @NotNull
    private Integer experienceInYears;
    @NotNull
    private BigDecimal hourlyRate;

    public workerResponsedto() {

    }
}
