package com.alergb.taskmanager.service;

import com.alergb.taskmanager.entity.Avatar;
import com.alergb.taskmanager.repository.AvatarRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class AvatarServiceImp implements AvatarService {

    private final AvatarRepository avatarRepository;


    @Override
    public Optional<Avatar> getAvatarById(Long id) {
        return avatarRepository.findById(id);
    }

    @Override
    public Avatar saveAvatar(Avatar avatar) {
        return avatarRepository.save(avatar);
    }
}
