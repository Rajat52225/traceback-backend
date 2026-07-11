package com.traceback.backend.repository;

import com.traceback.backend.model.Claim;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.traceback.backend.model.ClaimStatus;

import java.util.List;

public interface ClaimRepository extends MongoRepository<Claim, String> {

    List<Claim> findByItemId(String itemId);

    List<Claim> findByClaimantEmail(String claimantEmail);

    List<Claim> findByItemIdAndStatus(
            String itemId,
            ClaimStatus status
    );

    boolean existsByItemIdAndClaimantEmailAndStatus(
            String itemId,
            String claimantEmail,
            ClaimStatus status
    );
}