package com.example.booktracker.web;

import com.example.booktracker.domain.MediaType;
import com.example.booktracker.dto.SearchResult;
import com.example.booktracker.search.SearchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping
    public List<SearchResult> search(@RequestParam String q,
                                     @RequestParam(required = false) MediaType type) {
        if (q == null || q.isBlank()) return List.of();
        return searchService.search(q.trim(), type);
    }
}