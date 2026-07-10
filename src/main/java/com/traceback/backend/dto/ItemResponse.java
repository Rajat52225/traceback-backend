package com.traceback.backend.dto;

import com.traceback.backend.model.ItemStatus;
import com.traceback.backend.model.ItemType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class ItemResponse {

    private String id;
    private String title;
    private String description;
    private String category;

    private ItemType type;
    private ItemStatus status;

    private String location;
    private LocalDate eventDate;

    private String ownerEmail;

    private List<String> imageKeys;

    private LocalDateTime createdAt;
}