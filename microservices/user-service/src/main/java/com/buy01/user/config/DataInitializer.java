package com.buy01.user.config;

import com.buy01.user.model.Role;
import com.buy01.user.model.User;
import com.buy01.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        log.info("🔹 Vérification des utilisateurs de test...");

        // 1. Admin
        createUser("Admin", "User", "admin@example.com", "adminpassword123", Role.ADMIN);

        // 2. Vendeur
        createUser("Jane", "Seller", "jane.seller@example.com", "sellerpassword123", Role.SELLER);

        // 3. Client
        createUser("John", "Doe", "john.doe@example.com", "securepassword123", Role.CLIENT);

        log.info("✅ Initialisation terminée.");
    }

    private void createUser(String firstname, String lastname, String email, String password, Role role) {
        if (!userRepository.existsByEmail(email)) {
            User user = User.builder()
                    .firstname(firstname)
                    .lastname(lastname)
                    .email(email)
                    .password(passwordEncoder.encode(password))
                    .role(role)
                    .build();
            userRepository.save(user);
            log.info(" -> Utilisateur créé : {} ({})", email, role);
        }
    }
}
