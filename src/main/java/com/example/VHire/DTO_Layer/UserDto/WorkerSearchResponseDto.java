package com.example.vHire.dto_Layer.UserDto;

import com.example.vHire.dto_Layer.AvailabilityDto.AvailabilityRequestDto;
import com.example.vHire.dto_Layer.AvailabilityDto.AvailabilityResponseDto;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;



@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WorkerSearchResponseDto {
    private Long workerId;
    private String name ;
    private String city ;
    private Set<String> skills ;
    private Integer experienceYears ;
    private BigDecimal hourlyRate;
   private boolean available ;
   private List<AvailabilityResponseDto> availabilities;

}
