package com.alergb.taskmanager.mappers;

import com.alergb.taskmanager.dto.AvatarResponseDto;
import com.alergb.taskmanager.dto.UserRequestDto;
import com.alergb.taskmanager.dto.UserResponseDto;
import com.alergb.taskmanager.entity.Avatar;
import com.alergb.taskmanager.entity.Role;
import com.alergb.taskmanager.entity.Task;
import com.alergb.taskmanager.entity.User;
import com.alergb.taskmanager.exeptions.RoleNotFoundException;
import com.alergb.taskmanager.exeptions.TaskNotFoundException;
import com.alergb.taskmanager.repository.RoleRepository;
import com.alergb.taskmanager.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final RoleRepository roleRepository;
    private final TaskRepository taskRepository;

    public User toEntity(UserRequestDto requestDto){
        User entity = new User();

        entity.setName(requestDto.getName());
        entity.setEmail(requestDto.getEmail());
        entity.setPassword(requestDto.getPassword());

        entity.setRoles(mapRolesIdsToEntity(requestDto.getRolesId()));

        return entity;
    }

    private Set<Role> mapRolesIdsToEntity(Set<Long> ids){
        return ids.stream()
                .map(id -> roleRepository.findById(id)
                        .orElseThrow(() -> new RoleNotFoundException(id)))
                .collect(Collectors.toSet());
    }

    private Set<Task> mapTaskIdsToEntity(Set<Long> ids){

        return ids.stream()
                .map(id -> taskRepository.findById(id)
                        .orElseThrow(() -> new TaskNotFoundException(id)))
                .collect(Collectors.toSet());
    }


    public UserResponseDto toResponseDto(User user){
        UserResponseDto response = new UserResponseDto();

        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setTasks(user.getTasks());
        response.setRoles(user.getRoles());
        response.setCreatedAt(user.getCreatedAt());

        Avatar avatar = user.getAvatar();

        AvatarResponseDto avatarResponseDto = new AvatarResponseDto();
        avatarResponseDto.setId(avatar.getId());
        avatarResponseDto.setAvatarContentType(avatar.getAvatarContentType());

        response.setAvatar(avatarResponseDto);

        return response;
    }

    public Set<UserResponseDto> toResponseDtoList(List<User> users){
        Set<UserResponseDto> responseDtoList = new HashSet<>();
        users.forEach(user -> responseDtoList.add(toResponseDto(user)));

        return responseDtoList;
    }

}
