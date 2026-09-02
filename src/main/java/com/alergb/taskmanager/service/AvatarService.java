package com.alergb.taskmanager.service;


import com.alergb.taskmanager.entity.Avatar;

import java.util.Optional;

public interface AvatarService {
    Optional<Avatar> getAvatarById(Long id);
    Avatar saveAvatar(Avatar avatar);
}
