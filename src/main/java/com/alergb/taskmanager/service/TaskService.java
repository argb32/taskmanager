package com.alergb.taskmanager.service;

import com.alergb.taskmanager.entity.Task;

import java.util.List;

public interface TaskService {
    List<Task> getAllTasks();
    Task getTaskById (Long id);
    List<Task> findByCompleted(boolean completed);
    Task createTask(Task task);
    Task updateTask (Long id, Task updatedTask);
    void deleteTask (Long id);
    List<Task> findByTitleContainingIgnoreCase(String title);
    void removeUser (Long userId, Task task);
}
