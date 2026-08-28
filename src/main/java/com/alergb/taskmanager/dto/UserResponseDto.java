package com.alergb.taskmanager.dto;

import com.alergb.taskmanager.entity.Role;
import com.alergb.taskmanager.entity.Task;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
public class UserResponseDto {

    private Long id;
    private String name;
    private String email;
    private Set<Role> roles;
    private List<Task> tasks;
    private LocalDateTime createdAt;
}
