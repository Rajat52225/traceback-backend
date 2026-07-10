package com.traceback.backend.service;

import com.traceback.backend.dto.CreateItemRequest;
import com.traceback.backend.dto.ItemResponse;
import com.traceback.backend.model.Item;
import com.traceback.backend.model.ItemStatus;
import com.traceback.backend.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public ItemResponse createItem(
            CreateItemRequest request,
            String ownerEmail) {

        Item item = new Item();

        item.setTitle(request.getTitle());
        item.setDescription(request.getDescription());
        item.setCategory(request.getCategory());
        item.setType(request.getType());
        item.setLocation(request.getLocation());
        item.setEventDate(request.getEventDate());

        item.setStatus(ItemStatus.OPEN);
        item.setOwnerEmail(ownerEmail);
        item.setImageKeys(new ArrayList<>());
        item.setCreatedAt(LocalDateTime.now());

        Item savedItem = itemRepository.save(item);

        return toResponse(savedItem);
    }

    public List<ItemResponse> getAllItems() {

        return itemRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public ItemResponse getItemById(String id) {

        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        return toResponse(item);
    }

    private ItemResponse toResponse(Item item) {

        return new ItemResponse(
                item.getId(),
                item.getTitle(),
                item.getDescription(),
                item.getCategory(),
                item.getType(),
                item.getStatus(),
                item.getLocation(),
                item.getEventDate(),
                item.getOwnerEmail(),
                item.getImageKeys(),
                item.getCreatedAt()
        );
    }
}