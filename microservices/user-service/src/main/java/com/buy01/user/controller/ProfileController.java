package com.buy01.user.controller;

import com.buy01.user.model.User;
import com.buy01.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;

    // Existing endpoint in UserController might be here, checking for conflicts.
    // Assuming UserController handles /profile GET. We add PUT here or merge.

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUserProfile(@PathVariable String id, @RequestBody User userUpdates) {
        return ResponseEntity.ok(userService.updateUser(id, userUpdates));
    }
}
