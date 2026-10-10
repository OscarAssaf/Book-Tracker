package com.example.booktracker.dto;

import com.example.booktracker.domain.MediaType;

public record SearchResult(
        String source,
        String externalId,
        String title,
        String creators,
        MediaType type,
        String coverUrl,
        String description,
        Integer totalUnits
) {}