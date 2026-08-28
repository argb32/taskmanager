package com.alergb.taskmanager.controller;

import com.alergb.taskmanager.dto.UserRequestDto;
import com.alergb.taskmanager.dto.UserResponseDto;
import com.alergb.taskmanager.entity.Task;
import com.alergb.taskmanager.entity.User;
import com.alergb.taskmanager.mappers.UserMapper;
import com.alergb.taskmanager.service.TaskService;
import com.alergb.taskmanager.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final TaskService taskService;
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    public ResponseEntity<Set<UserResponseDto>> findAllUsers(){
        return ResponseEntity.ok(userMapper.toResponseDtoList(userService.findAllUsers()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> findById(Long id){
        return ResponseEntity.ok(userMapper.toResponseDto(userService.findUserById(id)));
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserRequestDto user){
        User savedUser = userService.createUser(userMapper.toEntity(user));

        UserResponseDto responseDto = userMapper.toResponseDto(savedUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(Long id, @Valid @RequestBody UserRequestDto user){
        User updatedUser = userService.updateUser(id, userMapper.toEntity(user));

        UserResponseDto responseDto = userMapper.toResponseDto(updatedUser);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(Long id){
        List<Task> tasks = userService.findUserById(id).getTasks();
        if (!tasks.isEmpty()){
            tasks.forEach(task -> taskService.removeUser(id, task));
        }
        userService.deleteUser(id);
    }
}
