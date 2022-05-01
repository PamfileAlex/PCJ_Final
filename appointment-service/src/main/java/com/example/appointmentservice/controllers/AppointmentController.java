package com.example.appointmentservice.controllers;

import com.example.appointmentservice.exceptions.AppointmentException;
import com.example.appointmentservice.exceptions.DuplicateAppointmentException;
import com.example.appointmentservice.exceptions.NotFoundException;
import com.example.appointmentservice.models.Appointment;
import com.example.appointmentservice.models.User;
import com.example.appointmentservice.services.AppointmentService;
import com.example.appointmentservice.services.UserCircuitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {
    private final AppointmentService appointmentService;
    private final UserCircuitService userCircuitService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService, UserCircuitService userCircuitService) {
        this.appointmentService = appointmentService;
        this.userCircuitService = userCircuitService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<Appointment> getAll() {
        return appointmentService.findAll();
    }

    //    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        Optional<Appointment> appointment = appointmentService.findById(id);
        if (appointment.isPresent()) {
            return ResponseEntity.ok(appointment.get());
        }
        return new ResponseEntity<>("Appointment with id " + id + " not found!", HttpStatus.NOT_FOUND);
    }

    //    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<?> save(@RequestBody Appointment appointment, BindingResult result) {
        if (result.hasErrors()) {
            throw new AppointmentException(HttpStatus.BAD_REQUEST, "Cannot create Appointment");
        }
        try {
            User user = userCircuitService.getById(appointment.getCarOwnerId());
            appointment.setCarOwner(user);

            return new ResponseEntity<>(appointmentService.save(appointment), HttpStatus.CREATED);
        } catch (DuplicateAppointmentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    //    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody Appointment updatedAppointment, @PathVariable String id) {
        try {
            return new ResponseEntity<>(appointmentService.update(updatedAppointment, id), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    //    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        try {
            appointmentService.delete(id);
            return new ResponseEntity<>("Successfully deleted", HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
