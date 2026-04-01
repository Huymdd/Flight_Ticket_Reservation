package com.airline.reservation.service;

import com.airline.reservation.entity.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    User register(User user);
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);
    List<User> findAll();
    User update(User user);
}
