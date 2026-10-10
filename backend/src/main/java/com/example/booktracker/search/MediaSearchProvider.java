package com.example.booktracker.search;

import com.example.booktracker.domain.MediaType;
import com.example.booktracker.dto.SearchResult;

import java.util.List;


public interface MediaSearchProvider {

   
    boolean supports(MediaType type);

    List<SearchResult> search(String query, MediaType type);
}