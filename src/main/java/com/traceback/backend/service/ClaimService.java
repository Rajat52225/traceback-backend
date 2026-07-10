package com.traceback.backend.service;

import com.traceback.backend.dto.ClaimResponse;
import com.traceback.backend.dto.CreateClaimRequest;
import com.traceback.backend.model.Claim;
import com.traceback.backend.model.ClaimStatus;
import com.traceback.backend.model.Item;
import com.traceback.backend.repository.ClaimRepository;
import com.traceback.backend.repository.ItemRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import com.traceback.backend.model.ItemStatus;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

import java.time.LocalDateTime;

@Service
public class ClaimService {

    private final ClaimRepository claimRepository;
    private final ItemRepository itemRepository;
    private final S3Service s3Service;

    public ClaimService(
            ClaimRepository claimRepository,
            ItemRepository itemRepository,
            S3Service s3Service) {

        this.claimRepository = claimRepository;
        this.itemRepository = itemRepository;
        this.s3Service = s3Service;
    }

    public ClaimResponse createClaim(
            String itemId,
            CreateClaimRequest request,
            String claimantEmail) {

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        if (item.getOwnerEmail().equals(claimantEmail)) {
            throw new RuntimeException("You cannot claim your own item");
        }

        Claim claim = new Claim();

        claim.setItemId(itemId);
        claim.setClaimantEmail(claimantEmail);
        claim.setMessage(request.getMessage());
        claim.setStatus(ClaimStatus.PENDING);
        claim.setCreatedAt(LocalDateTime.now());

        Claim savedClaim = claimRepository.save(claim);

        return toResponse(savedClaim);
    }

    public List<ClaimResponse> getClaimsForItem(
            String itemId,
            String ownerEmail) {

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        if (!item.getOwnerEmail().equals(ownerEmail)) {
            throw new RuntimeException(
                    "You are not allowed to view claims for this item"
            );
        }

        return claimRepository.findByItemId(itemId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public ClaimResponse updateClaimStatus(
            String claimId,
            ClaimStatus newStatus,
            String ownerEmail) {

        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new RuntimeException("Claim not found"));

        Item item = itemRepository.findById(claim.getItemId())
                .orElseThrow(() -> new RuntimeException("Item not found"));

        if (!item.getOwnerEmail().equals(ownerEmail)) {
            throw new RuntimeException(
                    "You are not allowed to update this claim"
            );
        }

        if (claim.getStatus() != ClaimStatus.PENDING) {
            throw new RuntimeException("Claim has already been processed");
        }

        claim.setStatus(newStatus);

        if (newStatus == ClaimStatus.ACCEPTED) {
            item.setStatus(ItemStatus.RESOLVED);
            itemRepository.save(item);
        }

        Claim savedClaim = claimRepository.save(claim);

        return toResponse(savedClaim);
    }

    public ClaimResponse uploadProofImage(
            String claimId,
            MultipartFile file,
            String claimantEmail) throws IOException {

        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new RuntimeException("Claim not found"));

        if (!claim.getClaimantEmail().equals(claimantEmail)) {
            throw new RuntimeException(
                    "You are not allowed to upload proof for this claim"
            );
        }

        if (claim.getStatus() != ClaimStatus.PENDING) {
            throw new RuntimeException(
                    "Cannot upload proof after claim has been processed"
            );
        }

        String imageKey = s3Service.uploadFile(file);

        claim.getProofImageKeys().add(imageKey);

        Claim savedClaim = claimRepository.save(claim);

        return toResponse(savedClaim);
    }

    private ClaimResponse toResponse(Claim claim) {

        return new ClaimResponse(
                claim.getId(),
                claim.getItemId(),
                claim.getClaimantEmail(),
                claim.getMessage(),
                claim.getProofImageKeys(),
                claim.getStatus(),
                claim.getCreatedAt()
        );
    }
}