package com.alergb.taskmanager.dto;

import com.alergb.taskmanager.entity.User;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@JsonPropertyOrder({
        "id",
        "title",
        "description",
        "completed",
        "createdAt"
})
public class TaskResponseDto {
    private Long id;
    private String title;
    private String description;
    private Boolean completed ;
    private LocalDateTime createdAt;
    private Set<UserResponseDto> assignedTo;

}
