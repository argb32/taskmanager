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

            User ale = getUser("ale@google.com");
            User sergio = getUser("sergio@google.com");
            User juan = getUser("juan@google.com");
            User maria = getUser("maria@google.com");
            User pedro = getUser("pedro@google.com");
            User laura = getUser("laura@google.com");
            User carlos = getUser("carlos@google.com");
            User ana = getUser("ana@google.com");
            User david = getUser("david@google.com");
            User lucia = getUser("lucia@google.com");
            User miguel = getUser("miguel@google.com");
            User elena = getUser("elena@google.com");
            User daniel = getUser("daniel@google.com");
            User sofia = getUser("sofia@google.com");
            User javier = getUser("javier@google.com");
            User paula = getUser("paula@google.com");
            User alvaro = getUser("alvaro@google.com");
            User claudia = getUser("claudia@google.com");
            User diego = getUser("diego@google.com");
            User marta = getUser("marta@google.com");
            User andres = getUser("andres@google.com");
            User irene = getUser("irene@google.com");
            User mario = getUser("mario@google.com");

            // 1 - Sin usuarios
            createTask(
                    "task1",
                    "Task sin usuarios asignados",
                    false,
                    List.of()
            );

            // 2 - Un usuario
            createTask(
                    "task2",
                    "Task asignada a Ale",
                    false,
                    List.of(ale)
            );

            // 3 - Varios usuarios
            createTask(
                    "task3",
                    "Task asignada a Ale y Sergio",
                    false,
                    List.of(ale, sergio)
            );

            // 4 - Un usuario
            createTask(
                    "task4",
                    "Preparar documentación del proyecto",
                    true,
                    List.of(juan)
            );

            // 5 - Sin usuarios
            createTask(
                    "task5",
                    "Revisar requisitos pendientes",
                    false,
                    List.of()
            );

            // 6 - Varios usuarios
            createTask(
                    "task6",
                    "Diseñar nueva interfaz",
                    false,
                    List.of(maria, pedro, laura)
            );

            // 7 - Un usuario
            createTask(
                    "task7",
                    "Configurar base de datos",
                    true,
                    List.of(carlos)
            );

            // 8 - Varios usuarios
            createTask(
                    "task8",
                    "Implementar autenticación",
                    false,
                    List.of(ana, david)
            );

            // 9 - Sin usuarios
            createTask(
                    "task9",
                    "Investigar nueva funcionalidad",
                    false,
                    List.of()
            );

            // 10 - Un usuario
            createTask(
                    "task10",
                    "Corregir errores del dashboard",
                    false,
                    List.of(lucia)
            );

            // 11 - Varios usuarios
            createTask(
                    "task11",
                    "Revisar código del backend",
                    true,
                    List.of(miguel, elena, daniel)
            );

            // 12 - Un usuario
            createTask(
                    "task12",
                    "Actualizar documentación",
                    false,
                    List.of(sofia)
            );

            // 13 - Sin usuarios
            createTask(
                    "task13",
                    "Crear estrategia de testing",
                    false,
                    List.of()
            );

            // 14 - Varios usuarios
            createTask(
                    "task14",
                    "Implementar sistema de notificaciones",
                    false,
                    List.of(javier, paula)
            );

            // 15 - Un usuario
            createTask(
                    "task15",
                    "Optimizar consultas SQL",
                    true,
                    List.of(alvaro)
            );

            // 16 - Sin usuarios
            createTask(
                    "task16",
                    "Revisar diseño de la aplicación",
                    false,
                    List.of()
            );

            // 17 - Varios usuarios
            createTask(
                    "task17",
                    "Crear tests unitarios",
                    false,
                    List.of(claudia, diego, marta)
            );

            // 18 - Un usuario
            createTask(
                    "task18",
                    "Configurar CI/CD",
                    false,
                    List.of(andres)
            );

            // 19 - Varios usuarios
            createTask(
                    "task19",
                    "Preparar despliegue a producción",
                    true,
                    List.of(irene, mario, ale)
            );

            // 20 - Sin usuarios
            createTask(
                    "task20",
                    "Analizar rendimiento de la aplicación",
                    false,
                    List.of()
            );

            // 21 - Un usuario
            createTask(
                    "task21",
                    "Actualizar dependencias",
                    false,
                    List.of(sergio)
            );

            // 22 - Varios usuarios
            createTask(
                    "task22",
                    "Revisar seguridad de la aplicación",
                    false,
                    List.of(juan, maria, carlos)
            );

            // 23 - Un usuario
            createTask(
                    "task23",
                    "Crear página de usuarios",
                    true,
                    List.of(laura)
            );

            // 24 - Sin usuarios
            createTask(
                    "task24",
                    "Añadir filtros al listado de tareas",
                    false,
                    List.of()
            );

            // 25 - Varios usuarios
            createTask(
                    "task25",
                    "Preparar versión final del proyecto",
                    false,
                    List.of(ale, sergio, juan, maria)
            );
        };
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException(
                        "User not found: " + email
                ));
    }

    private void createTask(
            String title,
            String description,
            boolean completed,
            List<User> assignedTo
    ) {
        if (!taskRepository.existsByTitle(title)) {

            Task task = new Task();

            task.setTitle(title);
            task.setDescription(description);
            task.setCompleted(completed);
            task.setAssignedTo(assignedTo);

            taskRepository.save(task);
        }
    }
}
