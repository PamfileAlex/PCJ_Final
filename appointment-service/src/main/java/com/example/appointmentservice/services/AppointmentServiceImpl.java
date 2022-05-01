package com.example.appointmentservice.services;

import com.example.appointmentservice.exceptions.DuplicateAppointmentException;
import com.example.appointmentservice.exceptions.NotFoundException;
import com.example.appointmentservice.models.Appointment;
import com.example.appointmentservice.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class AppointmentServiceImpl implements AppointmentService {
    private final List<Appointment> appointments;
    private final UserCircuitService userCircuitService;

    @Autowired
    public AppointmentServiceImpl(UserCircuitService userCircuitService) {
        this.userCircuitService = userCircuitService;
        appointments = getAppointments(false);
    }

    private List<Appointment> getAppointments(boolean empty) {
        if (empty) {
            return new ArrayList<>();
        }
        User carOwner = userCircuitService.getById("e9367753-f2b0-4537-b200-86f55711656f");
        return Stream.of(
                Appointment.builder()
                        .id(UUID.randomUUID().toString())
                        .car("Mazda 6")
                        .startDate(LocalDateTime.now())
                        .endDate(LocalDateTime.now().plusHours(2))
//                        .carOwner(User.builder()
//                                .id(UUID.randomUUID().toString())
//                                .email("test@mail.com")
//                                .address("Brasov")
//                                .firstName("Ionut")
//                                .lastName("Gica")
//                                .type("car owner")
//                                .build())
                        .carOwner(carOwner)
                        .carOwnerId(carOwner.getId())
                        .build()
        ).collect(Collectors.toList());
    }

    @Override
    public Optional<Appointment> findById(String id) {
        return appointments.stream()
                .filter(u -> id.equals(u.getId()))
                .findAny();
    }

    @Override
    public List<Appointment> findAll() {
        return appointments;
    }

    @Override
    public Appointment save(Appointment appointment) throws DuplicateAppointmentException {
        Optional<Appointment> optional = appointments.stream()
                .filter(a -> a.getStartDate().equals(appointment.getStartDate()))
                .findAny();

        if (optional.isPresent()) {
            throw new DuplicateAppointmentException("Appointment start date is occupied");
        }

        appointment.setId(UUID.randomUUID().toString());
        appointments.add(appointment);

        return appointment;
    }

    @Override
    public Appointment update(Appointment appointment, String id) throws NotFoundException {
        Appointment result = appointments.stream()
                .filter(u -> u.getId().equals(id)).findAny()
                .orElseThrow(() -> new NotFoundException(id));

        result.setCar(appointment.getCar());
        result.setStartDate(appointment.getStartDate());
        result.setEndDate(appointment.getEndDate());

        if (appointment.getCarOwner() == null) {
            result.setCarOwnerId(appointment.getCarOwnerId());
            result.setCarOwner(userCircuitService.getById(appointment.getCarOwnerId()));
        } else {
            result.setCarOwner(appointment.getCarOwner());
            result.setCarOwnerId(appointment.getCarOwner().getId());
        }

        return result;
    }

    @Override
    public void delete(String id) throws NotFoundException {
        Appointment appointment = appointments.stream().filter(u -> u.getId().equals(id))
                .findFirst().orElseThrow(() -> new NotFoundException(id));

        appointments.remove(appointment);
    }
}
