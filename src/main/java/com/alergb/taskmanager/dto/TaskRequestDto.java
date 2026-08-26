package com.alergb.taskmanager.dto;

import com.alergb.taskmanager.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Set;

@Data
public class TaskRequestDto {

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotNull
    private Boolean completed ;

    @NotNull
    private Set<Long> assignedToId;


}
