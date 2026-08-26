package com.alergb.taskmanager.mappers;

import com.alergb.taskmanager.dto.TaskRequestDto;
import com.alergb.taskmanager.dto.TaskResponseDto;
import com.alergb.taskmanager.entity.Task;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TaskMapper {
    public TaskResponseDto toResponseDto (Task task){
        TaskResponseDto responseDto = new TaskResponseDto();

        responseDto.setId(task.getId());
        responseDto.setTitle(task.getTitle());
        responseDto.setDescription(task.getDescription());
        responseDto.setCompleted(task.getCompleted());
        responseDto.setCreatedAt(task.getCreatedAt());
//        responseDto.setAssignedTo(task.getAssignedTo());

        return responseDto;
    }

    public List<TaskResponseDto> toResponseListDto(List<Task> tasks){
        return tasks.stream().map(this::toResponseDto).toList();
    }

    public Task toEntity (TaskRequestDto requestDto){
        Task entity = new Task();

        entity.setTitle(requestDto.getTitle());
        entity.setDescription(requestDto.getDescription());
        entity.setCompleted(requestDto.getCompleted());

        return entity;
    }


}
