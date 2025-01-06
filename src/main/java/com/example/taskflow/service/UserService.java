package com.example.taskflow.service;

import com.example.taskflow.model.Role;
import com.example.taskflow.model.User;
import com.example.taskflow.repository.RoleRepository;
import com.example.taskflow.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Проверка на существование пользователя
    public Optional<User> findByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        System.out.println("Checking username in database: " + username + " -> Exists: " + user.isPresent());
        return user;
    }

    // Сохранение нового пользователя
    public User save(User user) {
        System.out.println("Attempting to save user: " + user);

        // Проверка уникальности имени пользователя
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username is already taken: " + user.getUsername());
        }

        // Проверка, что роли переданы
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            throw new IllegalArgumentException("At least one role must be specified for the user.");
        }

        // Получение ролей из базы данных
        Set<Role> roles = new HashSet<>();
        for (Role role : user.getRoles()) {
            Role existingRole = roleRepository.findByName(role.getName())
                    .orElseThrow(() -> new RuntimeException("Role not found: " + role.getName()));
            roles.add(existingRole);
        }

        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        System.out.println("User before saving: " + user);

        // Сохранение пользователя
        User savedUser = userRepository.save(user);
        System.out.println("User saved successfully: " + savedUser);

        return savedUser;
    }
}
