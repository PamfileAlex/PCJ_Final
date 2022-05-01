package com.example.appointmentservice.services;

import com.example.appointmentservice.models.Appointment;

import java.util.List;
import java.util.Optional;

public interface AppointmentService {
    Optional<Appointment> findById(String id);
    List<Appointment> findAll();
    Appointment save(Appointment appointment);
    Appointment update(Appointment appointment, String id);
    void delete(String id);
}
