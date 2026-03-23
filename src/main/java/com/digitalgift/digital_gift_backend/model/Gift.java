package com.digitalgift.digital_gift_backend.model;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "gifts")
public class Gift {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(columnDefinition = "TEXT")
    private String message;
    
    @Column(name = "spotify_url", length = 500)
    private String spotifyUrl;
    
    @Column(name = "song_name", length = 200)
    private String songName;
    
    @Column(name = "artist_name", length = 200)
    private String artistName;
    
    @Column(name = "flower_type", length = 50)
    private String flowerType;
    
    @Column(name = "shareable_link", unique = true, nullable = false, length = 36)
    private String shareableLink;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    // Constructors
    public Gift() {
        this.shareableLink = UUID.randomUUID().toString(); // Auto-generate UUID
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
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


// GiftLanding.jsx receives data
// {
//   message: "Happy Birthday!",
//   spotifyUrl: "https://open.spotify.com/...",
//   songName: "Perfect",
//   artistName: "Ed Sheeran",
//   flowerType: "lily"
// }

// // Phases 0-3 play with this data
// ```

// ---

// ## Visual Flow Diagram
// ```
// 1. User creates gift
//    ↓
// 2. new Gift() constructor runs → UUID generated
//    ↓
// 3. Save to database with UUID as shareable_link
//    ↓
// 4. Return shareableLink to frontend
//    ↓
// 5. User shares link: tuliflo.com/gift/{UUID}
//    ↓
// 6. Receiver opens link
//    ↓
// 7. Frontend extracts UUID from URL
//    ↓
// 8. Frontend calls GET /api/gifts/{UUID}
//    ↓
// 9. Backend searches database by shareable_link
//    ↓
// 10. Returns gift data
//    ↓
// 11. Phases 0-3 animate with gift data
// ```

// ---

// ## Why UUID?

// **Alternative 1: Sequential IDs**
// ```
// tuliflo.com/gift/1
// tuliflo.com/gift/2
// tuliflo.com/gift/3
// ```
// ❌ Easy to guess → people can see others' gifts!

// **UUID (what we use):**
// ```
// tuliflo.com/gift/a1b2c3d4-e5f6-7890-abcd-ef1234567890