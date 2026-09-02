package com.alergb.taskmanager.repository;

import com.alergb.taskmanager.entity.Avatar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AvatarRepository extends JpaRepository<Avatar, Long> {
}
