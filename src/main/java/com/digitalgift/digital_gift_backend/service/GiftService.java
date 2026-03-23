package com.digitalgift.digital_gift_backend.service;

import com.digitalgift.digital_gift_backend.dto.GiftRequest;
import com.digitalgift.digital_gift_backend.model.Gift;
import com.digitalgift.digital_gift_backend.model.User;
import com.digitalgift.digital_gift_backend.repository.GiftRepository;
import com.digitalgift.digital_gift_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GiftService {
    
    @Autowired
    private GiftRepository giftRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    // Create new gift
    public Gift createGift(GiftRequest request, Long userId) {
        // Find the user who's creating the gift
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Create gift entity
        Gift gift = new Gift();
        gift.setUser(user);
        gift.setMessage(request.getMessage());
        gift.setSpotifyUrl(request.getSpotifyUrl());
        gift.setSongName(request.getSongName());
        gift.setArtistName(request.getArtistName());
        gift.setFlowerType(request.getFlowerType());
        // shareableLink auto-generated in Gift constructor
        
        // Save to database
        return giftRepository.save(gift);
    }
    
    // Get gift by shareable link (for receivers)
    public Gift getGiftByLink(String shareableLink) {
        return giftRepository.findByShareableLink(shareableLink)
                .orElseThrow(() -> new RuntimeException("Gift not found"));
    }
    
    // Get all gifts created by a user (for dashboard)
    public List<Gift> getUserGifts(Long userId) {
        return giftRepository.findByUserId(userId);
    }
}
// // Step 1: Verify user exists
// User user = userRepository.findById(userId)
//         .orElseThrow(() -> new RuntimeException("User not found"));
// // userId comes from JWT token (we'll see this in Controller)
// // If userId=5 doesn't exist in database, throw error

// // Step 2: Map GiftRequest → Gift entity
// Gift gift = new Gift();
// gift.setUser(user); // Links gift to user (foreign key user_id)
// gift.setMessage(request.getMessage()); // Copy data from DTO
// // ... rest of fields

// // Step 3: Save
// return giftRepository.save(gift);
// // Inserts into gifts table
// // Gift constructor already created UUID for shareableLink
// // Returns: Gift with id=1, shareableLink="a1b2c3d4-..."

// getGiftByLink() method:
// javareturn giftRepository.findByShareableLink(shareableLink)
//         .orElseThrow(() -> new RuntimeException("Gift not found"));
// // When receiver opens "tuliflo.com/gift/a1b2c3d4"
// // Frontend extracts "a1b2c3d4" and calls this API
// // If link exists → return Gift data
// // If link doesn't exist → 404 error
