package com.buy01.user.service;

import com.buy01.user.model.User;
import com.buy01.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final com.buy01.user.config.JwtUtils jwtUtils;

    /**
     * Authentifie un utilisateur et retourne un token JWT.
     */
    public com.buy01.user.dto.AuthResponse login(com.buy01.user.dto.LoginRequest request) {
        // 1. Chercher l'utilisateur
        User user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. Vérifier le mot de passe
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        // 3. Générer le token
        String token = jwtUtils.generateToken(user);

        return com.buy01.user.dto.AuthResponse.builder()
                .token(token)
                .build();
    }

    /**
     * Enregistre un nouvel utilisateur.
     * 
     * @param user L'utilisateur à créer (sans ID, avec mot de passe en clair)
     * @return L'utilisateur sauvegardé
     */
    public User registerUser(User user) {
        // 1. Vérifier si l'email existe déjà
        if (repository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists: " + user.getEmail());
        }

        // 2. Hacher le mot de passe (IMPORTANT pour la sécurité)
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // 3. Sauvegarder en base
        return repository.save(user);
    }

    public User updateUser(String id, User userUpdates) {
        User user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (userUpdates.getFirstname() != null)
            user.setFirstname(userUpdates.getFirstname());
        if (userUpdates.getLastname() != null)
            user.setLastname(userUpdates.getLastname());
        if (userUpdates.getAvatar() != null)
            user.setAvatar(userUpdates.getAvatar());

        return repository.save(user);
    }
}
