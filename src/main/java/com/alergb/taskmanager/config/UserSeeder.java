package com.alergb.taskmanager.config;

import com.alergb.taskmanager.entity.Role;
import com.alergb.taskmanager.entity.User;
import com.alergb.taskmanager.repository.RoleRepository;
import com.alergb.taskmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.List;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class UserSeeder {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Order(2)
    @Bean
    CommandLineRunner seedUsers() {
        return args -> {

            Role userRole = roleRepository.findByRole("USER")
                    .orElseThrow(() -> new RuntimeException("USER role not found"));

            Role adminRole = roleRepository.findByRole("ADMIN")
                    .orElseThrow(() -> new RuntimeException("ADMIN role not found"));

            Role moderatorRole = roleRepository.findByRole("MODERATOR")
                    .orElseThrow(() -> new RuntimeException("MODERATOR role not found"));

            List<User> users = List.of(
                    createUser("Ale", "ale@google.com", Set.of(userRole, adminRole)),
                    createUser("Sergio", "sergio@google.com", Set.of(userRole, moderatorRole)),
                    createUser("Juan", "juan@google.com", Set.of(userRole)),
                    createUser("María", "maria@google.com", Set.of(userRole)),
                    createUser("Pedro", "pedro@google.com", Set.of(userRole)),
                    createUser("Laura", "laura@google.com", Set.of(userRole)),
                    createUser("Carlos", "carlos@google.com", Set.of(userRole)),
                    createUser("Ana", "ana@google.com", Set.of(userRole)),
                    createUser("David", "david@google.com", Set.of(userRole)),
                    createUser("Lucía", "lucia@google.com", Set.of(userRole)),
                    createUser("Miguel", "miguel@google.com", Set.of(userRole)),
                    createUser("Elena", "elena@google.com", Set.of(userRole)),
                    createUser("Daniel", "daniel@google.com", Set.of(userRole)),
                    createUser("Sofía", "sofia@google.com", Set.of(userRole)),
                    createUser("Javier", "javier@google.com", Set.of(userRole)),
                    createUser("Paula", "paula@google.com", Set.of(userRole)),
                    createUser("Álvaro", "alvaro@google.com", Set.of(userRole)),
                    createUser("Claudia", "claudia@google.com", Set.of(userRole)),
                    createUser("Diego", "diego@google.com", Set.of(userRole)),
                    createUser("Marta", "marta@google.com", Set.of(userRole)),
                    createUser("Andrés", "andres@google.com", Set.of(userRole)),
                    createUser("Irene", "irene@google.com", Set.of(userRole)),
                    createUser("Mario", "mario@google.com", Set.of(userRole))
            );

            users.forEach(user -> {
                if (!userRepository.existsByEmail(user.getEmail())) {
                    userRepository.save(user);
                }
            });
        };
    }

    private User createUser(String name, String email, Set<Role> roles) {
        User user = new User();

        user.setName(name);
        user.setEmail(email);
        user.setPassword("password");
        user.setRoles(roles);

        return user;
    }
}


