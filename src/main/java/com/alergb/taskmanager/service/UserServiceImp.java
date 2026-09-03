package com.alergb.taskmanager.service;

import com.alergb.taskmanager.entity.Avatar;
import com.alergb.taskmanager.entity.User;
import com.alergb.taskmanager.exeptions.UserNotFoundException;
import com.alergb.taskmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

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

    public void uploadAvatar(Long id, MultipartFile file){

        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        if(file.isEmpty()) throw new IllegalArgumentException("Avatar no puede estar vacío");

        if(file.getContentType() == null || !file.getContentType().startsWith("image/")) throw new IllegalArgumentException("File must be an image");

        try{

            Avatar avatar = new Avatar();

            avatar.setAvatar(file.getBytes());
            avatar.setAvatarContentType(file.getContentType());

            user.setAvatar(avatar);

            userRepository.save(user);

        } catch (IOException e) {
            throw new RuntimeException("Error leyendo el avatar", e);
        }


    }

    public void deleteUser(Long id) {
        userRepository.delete(findUserById(id));
    }
}
