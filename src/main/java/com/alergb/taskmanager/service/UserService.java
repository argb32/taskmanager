package com.alergb.taskmanager.service;

import com.alergb.taskmanager.entity.User;

import java.util.List;
import java.util.Set;

public interface UserService {
    List<User> findAllUsers();
    User findUserById(Long id);
    User createUser (User user);
    User updateUser (Long id, User user);
    void deleteUser(Long id);
}
