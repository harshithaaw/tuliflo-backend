package com.digitalgift.digital_gift_backend.repository;

import com.digitalgift.digital_gift_backend.model.Gift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface GiftRepository extends JpaRepository<Gift, Long> {
    
    Optional<Gift> findByShareableLink(String shareableLink);
    List<Gift> findByUserIdOrderByCreatedAtDesc(Long userId);
}