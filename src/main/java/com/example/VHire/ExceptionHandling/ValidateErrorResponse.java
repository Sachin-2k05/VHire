package com.example.VHire.ExceptionHandling;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Getter
@Setter
public class ValidateErrorResponse {

        private int status;
        private String error;
        private Map<String, String> fieldErrors;
        private LocalDateTime timestamp;

        public ValidateErrorResponse(Map<String, String> fieldErrors) {
            this.status = 400;
            this.error = "VALIDATION_ERROR";
            this.fieldErrors = fieldErrors;
            this.timestamp = LocalDateTime.now();
        }

        // getters & setters
}
