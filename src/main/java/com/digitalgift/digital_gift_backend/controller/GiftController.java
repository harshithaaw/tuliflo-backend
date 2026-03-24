package com.digitalgift.digital_gift_backend.controller;

import com.digitalgift.digital_gift_backend.dto.GiftRequest;
import com.digitalgift.digital_gift_backend.dto.GiftResponse;
import com.digitalgift.digital_gift_backend.model.Gift;
import com.digitalgift.digital_gift_backend.service.GiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/gifts")
@CrossOrigin(origins = {
    "http://localhost:5173",
    "https://tuliflo-frontend.vercel.app",
    "https://tuliflo-backend.onrender.com"
})
public class GiftController {
    
    @Autowired
    private GiftService giftService;
    
    // Create new gift (authenticated users only - we'll add security later)
    @PostMapping("/create")
    public ResponseEntity<GiftResponse> createGift(
            @RequestBody GiftRequest request,
            @RequestHeader("User-Id") Long userId) {
        
        // Call service to create gift
        Gift gift = giftService.createGift(request, userId);
        
        // Convert Gift entity to GiftResponse DTO
        GiftResponse response = new GiftResponse(
            gift.getId(),
            gift.getMessage(),
            gift.getSpotifyUrl(),
            gift.getSongName(),
            gift.getArtistName(),
            gift.getFlowerType(),
            gift.getShareableLink(),
            gift.getCreatedAt()
        );
        
        return ResponseEntity.ok(response);
    }
    
    // Get gift by shareable link (public - no auth needed)
    @GetMapping("/{shareableLink}")
    public ResponseEntity<GiftResponse> getGift(@PathVariable String shareableLink) {
        // Find gift by link
        Gift gift = giftService.getGiftByLink(shareableLink);
        
        // Convert to DTO
        GiftResponse response = new GiftResponse(
            gift.getId(),
            gift.getMessage(),
            gift.getSpotifyUrl(),
            gift.getSongName(),
            gift.getArtistName(),
            gift.getFlowerType(),
            gift.getShareableLink(),
            gift.getCreatedAt()
        );
        
        return ResponseEntity.ok(response);
    }
    
    // Get all gifts for a user (for dashboard)
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<GiftResponse>> getUserGifts(@PathVariable Long userId) {
        List<Gift> gifts = giftService.getUserGifts(userId);
        
        // Convert List<Gift> to List<GiftResponse>
        List<GiftResponse> responses = gifts.stream()
                .map(gift -> new GiftResponse(
                    gift.getId(),
                    gift.getMessage(),
                    gift.getSpotifyUrl(),
                    gift.getSongName(),
                    gift.getArtistName(),
                    gift.getFlowerType(),
                    gift.getShareableLink(),
                    gift.getCreatedAt()
                ))
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(responses);
    }
}


// EXPLANATION:
// createGift() endpoint:
// java@PostMapping("/create")
// // POST /api/gifts/create

// @RequestBody GiftRequest request
// // Frontend sends gift data as JSON

// @RequestHeader("User-Id") Long userId
// // Frontend sends userId in HTTP header: "User-Id: 1"
// // Later we'll replace this with JWT authentication
// // For now, frontend manually sends userId

// // Example request:
// POST /api/gifts/create
// Headers: { "User-Id": "1" }
// Body: {
//   "message": "Happy Birthday!",
//   "spotifyUrl": "https://open.spotify.com/...",
//   "songName": "Perfect",
//   "artistName": "Ed Sheeran",
//   "flowerType": "lily"
// }

// // Response:
// {
//   "id": 1,
//   "message": "Happy Birthday!",
//   "shareableLink": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
//   ...
// }
// getGift() endpoint:
// java@GetMapping("/{shareableLink}")
// // GET /api/gifts/a1b2c3d4-e5f6-7890-abcd-ef1234567890

// @PathVariable String shareableLink
// // Extracts "a1b2c3d4-..." from URL
// // URL: /api/gifts/ABC123
// // shareableLink = "ABC123"

// // Frontend (receiver) calls:
// GET /api/gifts/a1b2c3d4-e5f6-7890-abcd-ef1234567890

// // Backend returns gift data:
// {
//   "id": 1,
//   "message": "Happy Birthday!",
//   "spotifyUrl": "...",
//   "shareableLink": "a1b2c3d4-...",
//   ...
// }
// getUserGifts() endpoint:
// java// Dashboard page calls this to show user's created gifts
// GET /api/gifts/user/1

// // Returns array:
// [
//   { "id": 1, "message": "Gift 1", ... },
//   { "id": 2, "message": "Gift 2", ... }
// ]

// // .stream().map() explained:
// List<Gift> gifts = [Gift1, Gift2, Gift3];
// // Convert each Gift to GiftResponse:
// gifts.stream()  // Start stream
//      .map(gift -> new GiftResponse(...))  // Convert each Gift
//      .collect(Collectors.toList());  // Collect back to List
// // Result: [GiftResponse1, GiftResponse2, GiftResponse3]