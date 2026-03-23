package com.digitalgift.digital_gift_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()  // ALLOW EVERYTHING for testing
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            );
        
        return http.build();
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*"));  // Allow all origins temporarily
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
// ```

// **EXPLANATION:**

// ### What is Spring Security?

// Spring Security = Gatekeeper for your API. Decides who can access what.

// **Without Security:**
// ```
// Anyone can call ANY endpoint → Chaos!
// ```

// **With Security:**
// ```
// Public endpoints: Anyone can access
// Protected endpoints: Need authentication (JWT token)

// Line-by-Line Breakdown:
// java@Configuration
// @EnableWebSecurity
// What it does:

// Tells Spring "This is a security configuration class"
// Enables Spring Security for the entire application


// java@Bean
// public SecurityFilterChain securityFilterChain(HttpSecurity http)
// What is a Bean?

// Bean = Object managed by Spring
// Spring creates this object automatically
// Used throughout the app wherever needed

// What is SecurityFilterChain?

// Chain of filters that check every HTTP request
// Like airport security checkpoints - each request goes through filters


// java.csrf(csrf -> csrf.disable())
// What is CSRF?

// CSRF = Cross-Site Request Forgery attack
// Example: Malicious site tricks you into sending requests to bank.com

// Why disable it?

// CSRF protection is for browser-based forms (cookies)
// We use JWT tokens (not cookies) → CSRF doesn't apply
// REST APIs don't need CSRF protection

// Is it safe to disable?

// YES for JWT-based APIs
// NO for cookie-based session apps


// java.authorizeHttpRequests(auth -> auth
//     .requestMatchers("/api/auth/**").permitAll()
//     .requestMatchers("/api/gifts/**").permitAll()
//     .anyRequest().authenticated()
// )
// ```
// **What this does:**

// **Line 1: `.requestMatchers("/api/auth/**").permitAll()`**
// ```
// // /api/auth/** means ALL endpoints under /api/auth/
// Examples:
// ✅ /api/auth/register → Public (anyone can register)
// ✅ /api/auth/login → Public (anyone can login)

// permitAll() = No authentication needed
// ```

// **Line 2: `.requestMatchers("/api/gifts/**").permitAll()`**
// ```
// /api/gifts/** means ALL endpoints under /api/gifts/
// Examples:
// ✅ POST /api/gifts/create → Public (for now, we'll protect this later)
// ✅ GET /api/gifts/{link} → Public (receivers don't have accounts!)
// ✅ GET /api/gifts/user/{id} → Public (for now, we'll protect this later)

// WHY PUBLIC?
// - Receivers don't have accounts, so they can't login
// - They just open a link and see the gift
// - We'll add proper auth later (only creator can see their gifts)
// ```

// **Line 3: `.anyRequest().authenticated()`**
// ```
// ALL other endpoints (not listed above) need authentication
// Example: If you add /api/admin/** later → Requires JWT token

// java.sessionManagement(session -> session
//     .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
// )
// ```
// **What are sessions?**

// **Traditional apps (stateful):**
// ```
// 1. User logs in
// 2. Server creates session, stores in memory
// 3. Server sends session ID cookie to browser
// 4. Browser sends cookie with every request
// 5. Server checks session ID to identify user
// ```

// **JWT apps (stateless - what we use):**
// ```
// 1. User logs in
// 2. Server creates JWT token, sends to frontend
// 3. Frontend stores token in localStorage
// 4. Frontend sends token in header with every request
// 5. Server validates token (no session storage needed!)
// Why STATELESS?

// No session storage on server = scalable (can run multiple servers)
// JWT token has all info needed = server doesn't remember you
// Better for REST APIs


// java@Bean
// public PasswordEncoder passwordEncoder() {
//     return new BCryptPasswordEncoder();
// }
// What is PasswordEncoder?

// Tool to hash passwords
// Used in AuthService.java when we call passwordEncoder.encode(password)

// What is BCrypt?

// Industry-standard password hashing algorithm
// One-way encryption (can't reverse it)

// Example:
// javaInput: "password123"
// Output: "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy"

// // Same password twice = different hashes (salt is random)
// "password123" → "$2a$10$ABC..."
// "password123" → "$2a$10$XYZ..."

// // But both verify as correct:
// passwordEncoder.matches("password123", "$2a$10$ABC...") → true
// passwordEncoder.matches("password123", "$2a$10$XYZ...") → true
// Why @Bean?

// Makes PasswordEncoder available for @Autowired injection
// AuthService uses @Autowired PasswordEncoder passwordEncoder
// Spring automatically injects this BCryptPasswordEncoder instance