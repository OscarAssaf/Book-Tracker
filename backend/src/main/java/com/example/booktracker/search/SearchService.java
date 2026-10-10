package com.example.booktracker.search;

import com.example.booktracker.domain.MediaType;
import com.example.booktracker.dto.SearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchService {

    private static final Logger log = LoggerFactory.getLogger(SearchService.class);

    private final List<MediaSearchProvider> providers;

    public SearchService(List<MediaSearchProvider> providers) {
        this.providers = providers;
    }


    public List<SearchResult> search(String query, MediaType type) {
        List<SearchResult> all = new ArrayList<>();
        for (MediaSearchProvider provider : providers) {
            if (!provider.supports(type)) continue;
            try {
                all.addAll(provider.search(query, type));
            } catch (RuntimeException e) {
                log.warn("Search provider {} failed: {}", provider.getClass().getSimpleName(), e.getMessage());
            }
        }
        return all;
    }
}