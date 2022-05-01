package com.example.appointmentservice.models;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Appointment {
    private String id;
    private String car;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private User carOwner;

    //used to create new appointment
    private String carOwnerId;
}
