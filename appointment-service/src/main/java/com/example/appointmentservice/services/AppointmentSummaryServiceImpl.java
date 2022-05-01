package com.example.appointmentservice.services;

import com.example.appointmentservice.exceptions.DuplicateAppointmentException;
import com.example.appointmentservice.exceptions.NotFoundException;
import com.example.appointmentservice.models.Appointment;
import com.example.appointmentservice.models.AppointmentSummary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AppointmentSummaryServiceImpl implements AppointmentSummaryService {
    private final List<AppointmentSummary> appointmentSummaries = new ArrayList<>();

    @Override
    public Optional<AppointmentSummary> findById(String id) {
        return appointmentSummaries.stream()
                .filter(a -> id.equals(a.getId()))
                .findAny();
    }

    @Override
    public List<AppointmentSummary> findAll() {
        return appointmentSummaries;
    }

    @Override
    public AppointmentSummary save(AppointmentSummary appointmentSummary) throws DuplicateAppointmentException {
//        Optional<AppointmentSummary> optional = appointmentSummaries.stream()
//                .findAny();
//
//        if (optional.isPresent()) {
//            throw new DuplicateAppointmentException("User with email or id already exists");
//        }

        appointmentSummary.setId(UUID.randomUUID().toString());
        appointmentSummaries.add(appointmentSummary);

        return appointmentSummary;
    }

    @Override
    public AppointmentSummary update(AppointmentSummary appointmentSummary, String id) throws NotFoundException {
        AppointmentSummary result = appointmentSummaries.stream()
                .filter(u -> u.getId().equals(id)).findAny()
                .orElseThrow(() -> new NotFoundException(id));

        result.setAppointment(appointmentSummary.getAppointment());
        result.setMechanic(appointmentSummary.getMechanic());
        result.setComment(appointmentSummary.getComment());
        result.setTotalCost(appointmentSummary.getTotalCost());

        return result;
    }

    @Override
    public void delete(String id) {
        AppointmentSummary appointmentSummary = appointmentSummaries.stream().filter(u -> u.getId().equals(id))
                .findFirst().orElseThrow(() -> new NotFoundException(id));

        appointmentSummaries.remove(appointmentSummary);
    }
}
