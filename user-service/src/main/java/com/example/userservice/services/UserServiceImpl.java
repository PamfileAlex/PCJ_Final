package com.example.userservice.services;

import com.example.userservice.exceptions.DuplicateUserException;
import com.example.userservice.exceptions.NotFoundException;
import com.example.userservice.models.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserServiceImpl implements UserService {
    private final List<User> users = Stream.of(
            User.builder()
                    .id("e9367753-f2b0-4537-b200-86f55711656f")
                    .email("test@mail.com")
                    .address("Brasov")
                    .firstName("Ionut")
                    .lastName("Gica")
                    .type("car owner")
                    .build()
    ).collect(Collectors.toList());

    @Override
    public Optional<User> findById(String id)
    {
        return users.stream()
                .filter(u -> id.equals(u.getId()))
                .findAny();
    }

    @Override
    public List<User> findAll()
    {
        return users;
    }

    @Override
    public User save(User user) throws DuplicateUserException
    {
        Optional<User> optional = users.stream()
                .filter(u -> u.getId().equals(user.getId()) || u.getEmail().equals(user.getEmail()))
                .findAny();

        if (optional.isPresent())
        {
            throw new DuplicateUserException("User with email or id already exists");
        }

        user.setId(UUID.randomUUID().toString());
        users.add(user);

        return user;
    }

    @Override
    public User update(User user, String id) throws NotFoundException
    {
        User result = users.stream()
                .filter(u -> u.getId().equals(id)).findAny()
                .orElseThrow(() -> new NotFoundException(id));

        result.setAddress(user.getAddress());
        result.setFirstName(user.getFirstName());
        result.setLastName(user.getLastName());
        result.setEmail(user.getEmail());
        result.setType(user.getType());

        return result;
    }

    @Override
    public void delete(String id) throws NotFoundException
    {
        User user = users.stream().filter(u -> u.getId().equals(id))
                .findFirst().orElseThrow(() -> new NotFoundException(id));

        users.remove(user);
    }
}
