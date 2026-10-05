package com.example.booktracker.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "media_items")
@Getter
@Setter
@NoArgsConstructor
public class MediaItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 300)
    private String title;

    @Column(length = 500)
    private String creators;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MediaType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ReadingStatus status = ReadingStatus.PLANNED;


    @Column(name = "cover_url", length = 1000)
    private String coverUrl;

    
    @Column(length = 10000)
    private String description;

    @Column(name = "external_source", length = 30)
    private String externalSource;

        @Column(name = "external_id", length = 100)
    private String externalId;

    @Column(name = "total_units")
    private Integer totalUnits;

    @Column(nullable = false)
    private Integer progress = 0;

        private Integer rating;

    @Column(length = 2000)
    private String notes;


    
        @Column(name = "added_at", nullable = false)
         private Instant addedAt = Instant.now();


        @Column(name = "updated_at", nullable = false)
        private Instant updatedAt = Instant.now();

}

