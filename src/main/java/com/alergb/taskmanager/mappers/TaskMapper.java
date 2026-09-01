package com.alergb.taskmanager.mappers;

import com.alergb.taskmanager.dto.TaskRequestDto;
import com.alergb.taskmanager.dto.TaskResponseDto;
import com.alergb.taskmanager.entity.Task;
import com.alergb.taskmanager.entity.User;
import com.alergb.taskmanager.exeptions.UserNotFoundException;
import com.alergb.taskmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class TaskMapper {

    private final UserRepository userRepository;



    public TaskResponseDto toResponseDto (Task task){

        TaskResponseDto responseDto = new TaskResponseDto();

        responseDto.setId(task.getId());
        responseDto.setTitle(task.getTitle());
        responseDto.setDescription(task.getDescription());
        responseDto.setStage(task.getStage());
        responseDto.setCompleted(task.getCompleted());
        responseDto.setCreatedAt(task.getCreatedAt());
        responseDto.setAssignedTo(task.getAssignedTo());

        return responseDto;
    }

    public List<TaskResponseDto> toResponseListDto(List<Task> tasks){
        return tasks.stream().map(this::toResponseDto).toList();
    }

    public Task toEntity (TaskRequestDto requestDto){
        Task entity = new Task();

        entity.setTitle(requestDto.getTitle());
        entity.setDescription(requestDto.getDescription());
        entity.setStage(requestDto.getStage());
        entity.setCompleted(requestDto.getCompleted());
        entity.setAssignedTo(mapIdsToUsers(requestDto.getAssignedToId()));

        return entity;
    }

    public List<User> mapIdsToUsers(Set<Long> ids){
        return ids.stream().map(id ->
                userRepository.findById(id)
                        .orElseThrow(() ->
                                new UserNotFoundException(id)))
                .toList();
    }
}
