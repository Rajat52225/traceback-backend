package com.traceback.backend.controller;

import com.traceback.backend.dto.CreateItemRequest;
import com.traceback.backend.dto.ItemResponse;
import com.traceback.backend.service.ItemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public ResponseEntity<List<ItemResponse>> getAllItems() {

        List<ItemResponse> items = itemService.getAllItems();

        return ResponseEntity.ok(items);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ItemResponse> getItemById(
            @PathVariable String id) {

        ItemResponse response = itemService.getItemById(id);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ItemResponse> createItem(
            @Valid @RequestBody CreateItemRequest request,
            Authentication authentication) {

        String ownerEmail = authentication.getName();

        ItemResponse response =
                itemService.createItem(request, ownerEmail);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}