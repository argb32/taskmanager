package com.alergb.taskmanager.controller;

import com.alergb.taskmanager.dto.UserRequestDto;
import com.alergb.taskmanager.dto.UserResponseDto;
import com.alergb.taskmanager.entity.User;
import com.alergb.taskmanager.mappers.UserMapper;
import com.alergb.taskmanager.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> findAllUsers(){
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

}
