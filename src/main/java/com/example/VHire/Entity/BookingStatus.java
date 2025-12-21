package com.example.VHire.Entity;


import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;



public enum BookingStatus {
    REQUESTED,
    ACCEPTED,
    REJECTED_BY_WORKER,
    AUTO_REJECTED,
    CANCELLED_BY_COMPANY,
    COMPLETED



}
