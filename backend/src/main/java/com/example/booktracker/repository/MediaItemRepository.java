package com.example.booktracker.repository;

import com.example.booktracker.domain.MediaItem;
import com.example.booktracker.domain.MediaType;
import com.example.booktracker.domain.ReadingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MediaItemRepository extends JpaRepository<MediaItem, Long> {

    @Query("""
            select m from MediaItem m
            where (:type is null or m.type = :type)
              and (:status is null or m.status = :status)
            order by m.updatedAt desc
            """)
    List<MediaItem> filter(@Param("type") MediaType type, @Param("status") ReadingStatus status);

    boolean existsByExternalSourceAndExternalId(String externalSource, String externalId);
}