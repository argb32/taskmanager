package com.alergb.taskmanager.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;


public enum Stage {
    NOT_STARTED,
    IN_PROGRESS,
    TESTING,
    COMPLETED
}
