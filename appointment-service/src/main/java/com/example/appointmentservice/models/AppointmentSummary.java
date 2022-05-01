package com.example.appointmentservice.models;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentSummary {
    private String id;
    private Appointment appointment;
    private User mechanic;
    private String comment;
    private float totalCost;

    //used to create new appointment summary
    private String appointmentId;
    private String mechanicId;
}
