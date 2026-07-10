package com.traceback.backend.repository;

import com.traceback.backend.model.Claim;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ClaimRepository extends MongoRepository<Claim, String> {

    List<Claim> findByItemId(String itemId);

    List<Claim> findByClaimantEmail(String claimantEmail);
}