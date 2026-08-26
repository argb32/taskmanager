package com.alergb.taskmanager.service;

import com.alergb.taskmanager.entity.User;
import com.alergb.taskmanager.exeptions.UserNotFoundException;
import com.alergb.taskmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImp implements UserService{

    private final UserRepository userRepository;

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(Long id, User updatedUser) {

        return userRepository.findById(id)
                .map(user -> {
                    user.setName(updatedUser.getName());
                    user.setEmail((updatedUser.getEmail()));
                    user.setTasks(updatedUser.getTasks());
                    user.setRoles(updatedUser.getRoles());

                    return user;
                }).orElseThrow(() -> new UserNotFoundException(id));
    }

    public void deleteUser(Long id) {
        userRepository.delete(findUserById(id));
    }
}
