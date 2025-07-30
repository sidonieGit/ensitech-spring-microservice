package com.ensitech.auth_service;

import com.ensitech.auth_service.model.entity.Role;
import com.ensitech.auth_service.model.entity.User;
import com.ensitech.auth_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Override
    public void run(String... args) {
        if (userRepository.findByEmail("admin@ensitech.com").isEmpty()) {
            User user = new User();
            // user.setUsername("admin");
            user.setEmail("admin@ensitech.com");
            user.setPassword(encoder.encode("admin123"));
            user.setRole(Role.SUPER_ADMIN);
            userRepository.save(user);
            System.out.println("SUPER_ADMIN créé avec succès");
        }
    }
}
