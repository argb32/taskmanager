package com.alergb.taskmanager.dto;

import com.alergb.taskmanager.entity.Stage;
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

    @NotBlank
    private Stage stage;

    @NotNull
    private Boolean completed ;

    private Set<Long> assignedToId;


}
