package com.buy01.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User {

    @Id
    private String id; // MongoDB ID

    private String firstname;
    private String lastname;

    @Indexed(unique = true)
    private String email;

    private String password; // Will be hashed

    private String avatar; // URL to avatar image

    private Role role;
}
