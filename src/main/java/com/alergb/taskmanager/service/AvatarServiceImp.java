package com.alergb.taskmanager.service;

import com.alergb.taskmanager.entity.Avatar;
import com.alergb.taskmanager.exeptions.AvatarNotFoundException;
import com.alergb.taskmanager.repository.AvatarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AvatarServiceImp implements AvatarService {

    private final AvatarRepository avatarRepository;

    @Override
    public Avatar getAvatarById(Long id) {
        return avatarRepository.findById(id).orElseThrow(() -> new AvatarNotFoundException(id));
    }
}
