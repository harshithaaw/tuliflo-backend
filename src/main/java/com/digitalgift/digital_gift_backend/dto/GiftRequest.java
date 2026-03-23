package com.digitalgift.digital_gift_backend.dto;

public class GiftRequest {
    
    private String message;
    private String spotifyUrl;
    private String songName;
    private String artistName;
    private String flowerType;
    
    // Note: NO shareableLink here!
    // Backend auto-generates UUID in Gift entity constructor
    
    public GiftRequest() {}
    
    public GiftRequest(String message, String spotifyUrl, String songName, 
                       String artistName, String flowerType) {
        this.message = message;
        this.spotifyUrl = spotifyUrl;
        this.songName = songName;
        this.artistName = artistName;
        this.flowerType = flowerType;
    }
    
    // Getters and Setters
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
}