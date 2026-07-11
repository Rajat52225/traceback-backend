package com.traceback.backend.dto;

import com.traceback.backend.model.ClaimStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class ClaimResponse {

    private String id;
    private String itemId;
    private String claimantEmail;
    private String message;
    private List<String> proofImageKeys;
    private ClaimStatus status;
    private LocalDateTime createdAt;

    private List<String> proofImageUrls;
}