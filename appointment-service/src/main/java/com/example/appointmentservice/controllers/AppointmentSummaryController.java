package com.example.appointmentservice.controllers;

import com.example.appointmentservice.exceptions.AppointmentException;
import com.example.appointmentservice.exceptions.DuplicateAppointmentException;
import com.example.appointmentservice.exceptions.NotFoundException;
import com.example.appointmentservice.models.Appointment;
import com.example.appointmentservice.models.AppointmentSummary;
import com.example.appointmentservice.models.User;
import com.example.appointmentservice.services.AppointmentService;
import com.example.appointmentservice.services.AppointmentSummaryService;
import com.example.appointmentservice.services.UserCircuitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/summaries")
public class AppointmentSummaryController {
    private final AppointmentService appointmentService;
    private final AppointmentSummaryService appointmentSummaryService;
    private final UserCircuitService userCircuitService;

    @Autowired
    public AppointmentSummaryController(AppointmentService appointmentService, AppointmentSummaryService appointmentSummaryService, UserCircuitService userCircuitService) {
        this.appointmentService = appointmentService;
        this.appointmentSummaryService = appointmentSummaryService;
        this.userCircuitService = userCircuitService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<AppointmentSummary> getAll() {
        return appointmentSummaryService.findAll();
    }

    //    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        Optional<AppointmentSummary> appointment = appointmentSummaryService.findById(id);
        if (appointment.isPresent()) {
            return ResponseEntity.ok(appointment.get());
        }
        return new ResponseEntity<>("AppointmentSummary with id " + id + " not found!", HttpStatus.NOT_FOUND);
    }

    //    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<?> save(@RequestBody AppointmentSummary appointmentSummary, BindingResult result) {
        if (result.hasErrors()) {
            throw new AppointmentException(HttpStatus.BAD_REQUEST, "Cannot create AppointmentSummary");
        }
        try {
            Optional<Appointment> appointment = appointmentService.findById(appointmentSummary.getAppointmentId());
            if (appointment.isEmpty()) {
                throw new NotFoundException(appointmentSummary.getAppointmentId());
            }
            appointmentSummary.setAppointment(appointment.get());

            User user = userCircuitService.getById(appointmentSummary.getMechanicId());
            appointmentSummary.setMechanic(user);

            return new ResponseEntity<>(appointmentSummaryService.save(appointmentSummary), HttpStatus.CREATED);
        } catch (DuplicateAppointmentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    //    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody AppointmentSummary updatedAppointmentSummary, @PathVariable String id) {
        try {
            return new ResponseEntity<>(appointmentSummaryService.update(updatedAppointmentSummary, id), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    //    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        try {
            appointmentSummaryService.delete(id);
            return new ResponseEntity<>("Successfully deleted", HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
