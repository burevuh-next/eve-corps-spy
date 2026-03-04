package com.example.backend.service;

import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend.dto.RegisterRequest;
import com.example.backend.dto.UserResponse;
import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse register(RegisterRequest request) {
        // Проверка уникальности email
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Такой Email уже существует");
        }

        // Хеширование пароля
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // Создание пользователя (конструктор User(String email, String passwordHash) должен быть в классе User)
        User user = new User(request.getEmail(), encodedPassword);
        User savedUser = userRepository.save(user);

        // Форматирование даты для ответа
        String formattedDate = DateTimeFormatter.ISO_INSTANT.format(savedUser.getCreatedAt());
        return new UserResponse(savedUser.getId(), savedUser.getEmail(), formattedDate);
    }
    
    public User authenticate(String email, String password) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new RuntimeException("Invalid password");
        }
        
        return user;
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));
    }
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}