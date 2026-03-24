package com.digitalgift.digital_gift_backend.controller;

import com.digitalgift.digital_gift_backend.config.JwtUtil;
import com.digitalgift.digital_gift_backend.dto.AuthResponse;
import com.digitalgift.digital_gift_backend.dto.LoginRequest;
import com.digitalgift.digital_gift_backend.dto.RegisterRequest;
import com.digitalgift.digital_gift_backend.model.User;
import com.digitalgift.digital_gift_backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {
    "http://localhost:5173",
    "https://tuliflo-frontend.vercel.app",
    "https://tuliflo-backend.onrender.com"
})
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        // Call service to create user
        User user = authService.register(request);
        
        // Generate JWT token
        String token = jwtUtil.generateToken(user.getUsername());
        
        // Create response DTO (no password!)
        AuthResponse response = new AuthResponse(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            token
        );
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        // Call service to verify credentials
        User user = authService.login(request);
        
        // Generate JWT token
        String token = jwtUtil.generateToken(user.getUsername());
        
        // Create response DTO
        AuthResponse response = new AuthResponse(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            token
        );
        
        return ResponseEntity.ok(response);
    }
}
// EXPLANATION:
// What is a Controller?
// Controller = Handles HTTP requests. It's the entry point of your API.
// Annotations explained:
// java@RestController
// // Marks this class as a REST API controller
// // Automatically converts Java objects to JSON

// @RequestMapping("/api/auth")
// // All endpoints in this class start with /api/auth
// // So @PostMapping("/login") becomes POST /api/auth/login

// @CrossOrigin(origins = "http://localhost:5173")
// // Allows React (running on port 5173) to call this API (running on port 8080)
// // Without this, browser blocks requests (CORS error)

// @PostMapping("/register")
// // This method handles POST requests to /api/auth/register

// @RequestBody RegisterRequest request
// // Converts incoming JSON to RegisterRequest object
// // Frontend sends: { "username": "john", "email": "john@example.com", "password": "pass123" }
// // Spring automatically creates RegisterRequest object with these values

// ResponseEntity<AuthResponse>
// // HTTP response wrapper
// // ResponseEntity.ok(response) → HTTP 200 OK with JSON body
// register() flow:
// java// Frontend sends POST to /api/auth/register with JSON:
// {
//   "username": "john",
//   "email": "john@example.com",
//   "password": "pass123"
// }

// // Step 1: Spring converts JSON → RegisterRequest object
// RegisterRequest request = new RegisterRequest("john", "john@example.com", "pass123");

// // Step 2: Call service to create user
// User user = authService.register(request);
// // Service hashes password, saves to database, returns User

// // Step 3: Generate JWT token
// String token = jwtUtil.generateToken(user.getUsername());
// // Creates token like: "eyJhbGciOiJIUzI1NiIsInR5..."

// // Step 4: Create response WITHOUT password
// AuthResponse response = new AuthResponse(
//     user.getId(),           // 1
//     user.getUsername(),     // "john"
//     user.getEmail(),        // "john@example.com"
//     token                   // "eyJhbGciOiJIUzI1NiIsInR5..."
// );
// // Notice: NO user.getPassword() - we never send password back!

// // Step 5: Send response
// return ResponseEntity.ok(response);
// // Returns HTTP 200 OK with JSON:
// {
//   "userId": 1,
//   "username": "john",
//   "email": "john@example.com",
//   "token": "eyJhbGciOiJIUzI1NiIsInR5..."
// }

// // Frontend stores this token and uses it for future requests
// Why use DTOs now?
// java// WRONG: Return entity directly
// return ResponseEntity.ok(user);
// // Sends: { "id": 1, "username": "john", "password": "$2a$10$...", "gifts": [...] }
// // ❌ Password exposed! ❌ Extra data sent!

// // RIGHT: Return DTO
// return ResponseEntity.ok(response);
// // Sends: { "userId": 1, "username": "john", "email": "...", "token": "..." }
// // ✅ No password ✅ Only necessary data ✅ Includes token