package com.example.userservice.services;

import com.example.userservice.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> findById(String id);
    List<User> findAll();
    User save(User user);
    User update(User user, String id);
    void delete(String id);
}
