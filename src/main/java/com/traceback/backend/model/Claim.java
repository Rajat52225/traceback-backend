package com.traceback.backend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "claims")
public class Claim {

    @Id
    private String id;

    private String itemId;

    private String claimantEmail;

    private String message;

    private List<String> proofImageKeys = new ArrayList<>();

    private ClaimStatus status;

    private LocalDateTime createdAt;
}