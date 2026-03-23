package com.digitalgift.digital_gift_backend.dto;

public class AuthResponse {
    
    private Long userId;
    private String username;
    private String email;
    private String token; // JWT token for authentication
    
    public AuthResponse() {}
    
    public AuthResponse(Long userId, String username, String email, String token) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.token = token;
    }
    
    // Getters and Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}

// Backend sends this
// return new AuthResponse(user.getId(), user.getUsername(), user.getEmail(), jwtToken);

// // React receives:
// {
//   userId: 1,
//   username: 'john',
//   email: 'john@example.com',
//   token: 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...'
// }
// // React stores token in localStorage for future requests