package com.example.booktracker.service;

import com.example.booktracker.domain.MediaItem;
import com.example.booktracker.domain.MediaType;
import com.example.booktracker.domain.ReadingStatus;
import com.example.booktracker.dto.MediaItemRequest;
import com.example.booktracker.dto.MediaItemResponse;
import com.example.booktracker.exception.ConflictException;
import com.example.booktracker.repository.MediaItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.booktracker.exception.NotFoundException;

import java.time.Instant;
import java.util.List;

@Service
@Transactional
public class MediaItemService {

    private final MediaItemRepository repository;

    public MediaItemService(MediaItemRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<MediaItemResponse> list(MediaType type, ReadingStatus status) {
        return repository.filter(type, status).stream().map(MediaItemResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public MediaItemResponse get(Long id) {
        return MediaItemResponse.from(find(id));
    }

    public MediaItemResponse create(MediaItemRequest request) {
    if (request.externalSource() != null && request.externalId() != null
         && repository.existsByExternalSourceAndExternalId(request.externalSource(), request.externalId())) {
     throw new ConflictException("This item is already in your library");
    }

    MediaItem item = new MediaItem();
    apply(item, request);
    return MediaItemResponse.from(repository.save(item));
    }


    public MediaItemResponse update(Long id, MediaItemRequest request) {
        MediaItem item = find(id);
        apply(item, request);
        return MediaItemResponse.from(item);
    }

    public void delete(Long id) {
        repository.delete(find(id));
    }

    private MediaItem find(Long id) {
    return repository.findById(id)
            .orElseThrow(() -> new NotFoundException("Item " + id + " not found"));
        }

    private void apply(MediaItem item, MediaItemRequest r) {
        item.setTitle(r.title());
        item.setCreators(r.creators());
        item.setType(r.type());
        item.setStatus(r.status());
        item.setCoverUrl(r.coverUrl());
        item.setDescription(r.description());
        item.setTotalUnits(r.totalUnits());
        item.setProgress(r.progress() == null ? 0 : r.progress());
        item.setRating(r.rating());
        item.setNotes(r.notes());
        item.setExternalSource(r.externalSource());
        item.setExternalId(r.externalId());
        item.setUpdatedAt(Instant.now());
    }
}