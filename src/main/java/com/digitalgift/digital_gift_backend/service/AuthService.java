package com.digitalgift.digital_gift_backend.service;

import com.digitalgift.digital_gift_backend.dto.LoginRequest;
import com.digitalgift.digital_gift_backend.dto.RegisterRequest;
import com.digitalgift.digital_gift_backend.model.User;
import com.digitalgift.digital_gift_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    // Register new user
    public User register(RegisterRequest request) {
        // Check if username already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        
        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        
        // Create new user
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // Hash password
        
        // Save to database
        return userRepository.save(user);
    }
    
    // Login user
    public User login(LoginRequest request) {
        // Find user by username
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Check if password matches
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        
        return user;
    }
}




// EXPLANATION:
// What is a Service Layer?
// Service = Business logic. It sits between Controller (handles HTTP) and Repository (handles database).
// Flow: Controller → Service → Repository → Database
// register() method breakdown:
// java// Step 1: Check duplicates
// if (userRepository.existsByUsername(request.getUsername())) {
//     throw new RuntimeException("Username already exists");
// }
// // If username "john" exists, stop here and send error

// // Step 2: Hash password
// user.setPassword(passwordEncoder.encode(request.getPassword()));
// // Input: "password123"
// // Output: "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy"
// // This is BCrypt hashing - one-way encryption

// // Step 3: Save to database
// return userRepository.save(user);
// // Inserts into users table, returns saved User with auto-generated id
// login() method breakdown:
// java// Step 1: Find user
// User user = userRepository.findByUsername(request.getUsername())
//         .orElseThrow(() -> new RuntimeException("User not found"));
// // If username doesn't exist, throw error

// // Step 2: Verify password
// if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
//     throw new RuntimeException("Invalid password");
// }
// // passwordEncoder.matches("password123", "$2a$10$N9qo8...") → true/false
// // Compares plain text password with hashed database password

// // Step 3: Return user
// return user;
// // If everything passes, return User object (Controller will create JWT)
// Why hash passwords?
// If someone hacks your database, they see:

// ❌ Without hashing: password123 (they can use it!)
// ✅ With hashing: $2a$10$N9qo8... (useless, can't reverse it)

