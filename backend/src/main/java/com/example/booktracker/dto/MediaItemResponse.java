package com.example.booktracker.dto;

import com.example.booktracker.domain.MediaItem;
import com.example.booktracker.domain.MediaType;
import com.example.booktracker.domain.ReadingStatus;

import java.time.Instant;

public record MediaItemResponse(
        Long id,
        String title,
        String creators,
        MediaType type,
        ReadingStatus status,
        String coverUrl,
        String description,
        String externalSource,
        String externalId,
        Integer totalUnits,
        Integer progress,
        Integer rating,
        String notes,
        Instant addedAt,
        Instant updatedAt
) {
    public static MediaItemResponse from(MediaItem m) {
        return new MediaItemResponse(m.getId(), m.getTitle(), m.getCreators(), m.getType(),
                m.getStatus(), m.getCoverUrl(), m.getDescription(), m.getExternalSource(),
                m.getExternalId(), m.getTotalUnits(), m.getProgress(), m.getRating(),
                m.getNotes(), m.getAddedAt(), m.getUpdatedAt());
    }
}