package com.traceback.backend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    @Id
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