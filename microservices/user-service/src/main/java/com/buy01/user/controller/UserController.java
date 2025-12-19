package com.buy01.user.controller;

import com.buy01.user.model.User;
import com.buy01.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository repository;

    @GetMapping("/profile")
    public ResponseEntity<User> getProfile() {
        // 1. Récupérer l'utilisateur courant du contexte de sécurité (posé par le JWT
        // Filter)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // C'est un test pour voir si le filtre a marché
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Not authenticated");
        }

        // 2. Extraire l'email (UserDetailsService a mis un objet UserDetails comme
        // Principal)
        org.springframework.security.core.userdetails.UserDetails userDetails = (UserDetails) authentication
                .getPrincipal();

        // 3. Chercher l'user complet (pour avoir nom, prenom etc)
        User user = repository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Masquer le password avant renvoi (bonne pratique)
        user.setPassword(null);

        return ResponseEntity.ok(user);
    }
}
