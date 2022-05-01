package com.example.userservice.controllers;

import com.example.userservice.exceptions.DuplicateUserException;
import com.example.userservice.exceptions.NotFoundException;
import com.example.userservice.exceptions.UserException;
import com.example.userservice.models.User;
import com.example.userservice.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<User> getAll() {
        return userService.findAll();
    }

//    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        }
        return new ResponseEntity<>("User with id " + id + " not found!", HttpStatus.NOT_FOUND);
    }

//    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<?> save(@RequestBody User user, BindingResult result) {
        if (result.hasErrors()) {
            throw new UserException(HttpStatus.BAD_REQUEST, "Cannot create user");
        }
        try {
            return new ResponseEntity<>(userService.save(user), HttpStatus.CREATED);
        } catch (DuplicateUserException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

//    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody User updatedUser, @PathVariable String id) {
        try {
            return new ResponseEntity<>(userService.update(updatedUser, id), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

//    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        try {
            userService.delete(id);
            return new ResponseEntity<>("Successfully deleted", HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
