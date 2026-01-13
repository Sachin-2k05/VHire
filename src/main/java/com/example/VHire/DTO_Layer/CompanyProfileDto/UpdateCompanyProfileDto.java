package com.example.vHire.dto_Layer.CompanyProfileDto;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateCompanyProfileDto {
    private Long companyId;
    private String companyName;
    private String industry;
    private String description;
    private String website;
    private boolean active;
}
