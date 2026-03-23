package com.digitalgift.digital_gift_backend.dto;

import java.time.LocalDateTime;

public class GiftResponse {
    private Long id;
    private String message;
    private String spotifyUrl;
    private String songName;
    private String artistName;
    private String flowerType;
    private String shareableLink; // This is what receiver uses!
    private LocalDateTime createdAt;

      // Note: NO userId here for security
    // Receiver doesn't need to know who created it
    
    public GiftResponse() {}

    public GiftResponse(Long id, String message, String spotifyUrl, String songName, 
                       String artistName, String flowerType, String shareableLink, LocalDateTime createdAt) {
        this.id = id;
        this.message = message;
        this.spotifyUrl = spotifyUrl;
        this.songName = songName;
        this.artistName = artistName;
        this.flowerType = flowerType;
        this.shareableLink = shareableLink;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public String getSpotifyUrl() { return spotifyUrl; }
    public void setSpotifyUrl(String spotifyUrl) { this.spotifyUrl = spotifyUrl; }
    
    public String getSongName() { return songName; }
    public void setSongName(String songName) { this.songName = songName; }
    
    public String getArtistName() { return artistName; }
    public void setArtistName(String artistName) { this.artistName = artistName; }
    
    public String getFlowerType() { return flowerType; }
    public void setFlowerType(String flowerType) { this.flowerType = flowerType; }
    
    public String getShareableLink() { return shareableLink; }
    public void setShareableLink(String shareableLink) { this.shareableLink = shareableLink; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

}
// User creates gift → Frontend sends GiftRequest
// Backend creates Gift entity → Converts to GiftResponse
// Returns: { id: 1, shareableLink: "abc-123-def", ... }
// User shares link: "tuliflo.com/gift/abc-123-def"