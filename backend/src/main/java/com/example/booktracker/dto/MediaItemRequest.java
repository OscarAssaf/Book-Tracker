package com.example.booktracker.dto;
import com.example.booktracker.domain.MediaType;
import com.example.booktracker.domain.ReadingStatus;

import jakarta.validation.constraints.*;

public record MediaItemRequest(
        @NotBlank @Size(max = 300) String title,
        @Size(max = 500) String creators,
        @NotNull MediaType type,
        @NotNull ReadingStatus status,
        String coverUrl,
        String description,
        @Min(0) Integer totalUnits,
        @Min(0) Integer progress,
        @Min(1) @Max(10) Integer rating,
        @Size(max = 2000) String notes,
        String externalSource,
        String externalId
) {}