package com.buy01.user.repository;

import com.buy01.user.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    // Pour trouver un utilisateur par son email (authentification)
    Optional<User> findByEmail(String email);

    // Vérifier si un email existe déjà (inscription)
    boolean existsByEmail(String email);
}
