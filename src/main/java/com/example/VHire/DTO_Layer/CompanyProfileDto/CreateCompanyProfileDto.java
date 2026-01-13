package com.example.vHire.dto_Layer.CompanyProfileDto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateCompanyProfileDto {
    @NotBlank
    private String companyName;

    private String industry;
    private String description;
    private String website;
}
