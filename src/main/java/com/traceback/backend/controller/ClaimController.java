package com.traceback.backend.controller;

import com.traceback.backend.dto.ClaimResponse;
import com.traceback.backend.dto.CreateClaimRequest;
import com.traceback.backend.service.ClaimService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.traceback.backend.model.ClaimStatus;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@RestController
@RequestMapping("/api/claims")
public class ClaimController {

    private final ClaimService claimService;

    public ClaimController(ClaimService claimService) {
        this.claimService = claimService;
    }

    @GetMapping("/items/{itemId}")
    public ResponseEntity<List<ClaimResponse>> getClaimsForItem(
            @PathVariable String itemId,
            Authentication authentication) {

        String ownerEmail = authentication.getName();

        List<ClaimResponse> claims =
                claimService.getClaimsForItem(itemId, ownerEmail);

        return ResponseEntity.ok(claims);
    }

    @PostMapping("/items/{itemId}")
    public ResponseEntity<ClaimResponse> createClaim(
            @PathVariable String itemId,
            @Valid @RequestBody CreateClaimRequest request,
            Authentication authentication) {

        String claimantEmail = authentication.getName();

        ClaimResponse response =
                claimService.createClaim(
                        itemId,
                        request,
                        claimantEmail
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PatchMapping("/{claimId}/status")
    public ResponseEntity<ClaimResponse> updateClaimStatus(
            @PathVariable String claimId,
            @RequestParam ClaimStatus status,
            Authentication authentication) {

        String ownerEmail = authentication.getName();

        ClaimResponse response =
                claimService.updateClaimStatus(
                        claimId,
                        status,
                        ownerEmail
                );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{claimId}/proof-images")
    public ResponseEntity<ClaimResponse> uploadProofImage(
            @PathVariable String claimId,
            @RequestParam("file") MultipartFile file,
            Authentication authentication) throws IOException {

        String claimantEmail = authentication.getName();

        ClaimResponse response =
                claimService.uploadProofImage(
                        claimId,
                        file,
                        claimantEmail
                );

        return ResponseEntity.ok(response);
    }
}