package com.alergb.taskmanager.config;

import com.alergb.taskmanager.entity.Task;
import com.alergb.taskmanager.entity.User;
import com.alergb.taskmanager.repository.TaskRepository;
import com.alergb.taskmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class TaskSeeder {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Bean
    @Order(3)
    CommandLineRunner seedTasks() {
        return args -> {

            User ale = userRepository.findByEmail("ale@google.com")
                    .orElseThrow(() -> new RuntimeException("Ale user not found"));

            User sergio = userRepository.findByEmail("sergio@google.com")
                    .orElseThrow(() -> new RuntimeException("Sergio user not found"));

            // Task sin usuarios asignados
            if (!taskRepository.existsByTitle("task1")) {
                Task task1 = new Task();
                task1.setTitle("task1");
                task1.setDescription("Task sin usuarios asignados");
                task1.setCompleted(false);

                taskRepository.save(task1);
            }

            // Task asignada a Ale
            if (!taskRepository.existsByTitle("task2")) {
                Task task2 = new Task();
                task2.setTitle("task2");
                task2.setDescription("Task asignada a Ale");
                task2.setCompleted(false);
                task2.setAssignedTo(List.of(ale));

                taskRepository.save(task2);
            }

            // Task asignada a Ale y Sergio
            if (!taskRepository.existsByTitle("task3")) {
                Task task3 = new Task();
                task3.setTitle("task3");
                task3.setDescription("Task asignada a Ale y Sergio");
                task3.setCompleted(false);
                task3.setAssignedTo(List.of(ale, sergio));

                taskRepository.save(task3);
            }
        };
    }
}
