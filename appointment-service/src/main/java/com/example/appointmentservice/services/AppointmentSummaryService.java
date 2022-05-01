package com.example.appointmentservice.services;

import com.example.appointmentservice.models.AppointmentSummary;

import java.util.List;
import java.util.Optional;

public interface AppointmentSummaryService {
    Optional<AppointmentSummary> findById(String id);
    List<AppointmentSummary> findAll();
    AppointmentSummary save(AppointmentSummary appointmentSummary);
    AppointmentSummary update(AppointmentSummary appointmentSummary, String id);
    void delete(String id);
}
