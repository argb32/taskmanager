package com.alergb.taskmanager.config;

import com.alergb.taskmanager.entity.Role;
import com.alergb.taskmanager.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
@RequiredArgsConstructor
public class RoleSeeder {

    private final RoleRepository roleRepository;

    @Bean
    @Order(1)
    CommandLineRunner seedRoles() {
        return args -> {
            createRoleIfNotExists("USER");
            createRoleIfNotExists("ADMIN");
            createRoleIfNotExists("MODERATOR");
        };
    }

    private void createRoleIfNotExists(String role) {
        if (!roleRepository.existsByRole(role)) {
            roleRepository.save(new Role(null, role));
        }
    }
}