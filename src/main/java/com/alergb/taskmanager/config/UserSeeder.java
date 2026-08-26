package com.alergb.taskmanager.config;

import com.alergb.taskmanager.entity.Role;
import com.alergb.taskmanager.entity.User;
import com.alergb.taskmanager.repository.RoleRepository;
import com.alergb.taskmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class UserSeeder {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Bean
    CommandLineRunner seedUsers() {
        return args -> {

            Role userRole = roleRepository.findByRole("USER")
                    .orElseThrow(() -> new RuntimeException("USER role not found"));

            Role adminRole = roleRepository.findByRole("ADMIN")
                    .orElseThrow(() -> new RuntimeException("ADMIN role not found"));

            Role moderatorRole = roleRepository.findByRole("MODERATOR")
                    .orElseThrow(() -> new RuntimeException("MODERATOR role not found"));

            if (!userRepository.existsByEmail("ale@google.com")) {

                User ale = new User();
                ale.setName("ALe");
                ale.setEmail("ale@google.com");
                ale.setPassword("password");
                ale.setRoles(Set.of(userRole, adminRole));

                userRepository.save(ale);
            }

            if (!userRepository.existsByEmail("sergio@google.com")) {

                User sergio = new User();
                sergio.setName("Sergio");
                sergio.setEmail("sergio@google.com");
                sergio.setPassword("password");
                sergio.setRoles(Set.of(moderatorRole));

                userRepository.save(sergio);
            }
        };
    }
}

