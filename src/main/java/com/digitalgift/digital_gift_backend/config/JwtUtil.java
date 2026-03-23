package com.digitalgift.digital_gift_backend.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    
    @Value("${jwt.secret}")
    private String secret;
    
    @Value("${jwt.expiration}")
    private Long expiration;
    
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
    
    // Generate token from username
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }
    
    // Create JWT token with claims and subject (UPDATED FOR v0.12.3)
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .claims(claims)  // ✅ Changed from setClaims
                .subject(subject)  // ✅ Changed from setSubject
                .issuedAt(new Date(System.currentTimeMillis()))  // ✅ Changed from setIssuedAt
                .expiration(new Date(System.currentTimeMillis() + expiration))  // ✅ Changed from setExpiration
                .signWith(getSigningKey())  // ✅ Removed SignatureAlgorithm (auto-detected)
                .compact();
    }
    
    // Extract username from token
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }
    
    // Extract expiration date
    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }
    
    // Parse and extract all claims from token (UPDATED FOR v0.12.3)
    private Claims extractAllClaims(String token) {
        return Jwts.parser()  // ✅ Changed from parserBuilder()
                .verifyWith(getSigningKey())  // ✅ Changed from setSigningKey
                .build()
                .parseSignedClaims(token)  // ✅ Changed from parseClaimsJws
                .getPayload();  // ✅ Changed from getBody
    }
    
    // Check if token is expired
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    
    // Validate token against username and expiration
    public Boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }
}
// ```

// **EXPLANATION:**

// **What is JWT?**
// JWT = JSON Web Token. It's a string that looks like this:
// ```
// eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqb2huIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c

// Structure: HEADER.PAYLOAD.SIGNATURE

// Header: Algorithm used (HS256)
// Payload: Data (username, expiration)
// Signature: Encrypted hash to verify authenticity

// How it works:

// User logs in → Backend generates token with generateToken("john")
// Frontend stores token in localStorage
// Every API call → Frontend sends token in header: Authorization: Bearer <token>
// Backend validates token with validateToken(token, "john")
// If valid → Allow access, if invalid → 401 Unauthorized

// Key methods:

// generateToken() - Creates token when user logs in
// extractUsername() - Gets username from token
// validateToken() - Checks if token is valid and not expired
// getSigningKey() - Uses your secret key to encrypt/decryp