package com.example.booktracker.search;

import com.example.booktracker.domain.MediaType;
import com.example.booktracker.dto.SearchResult;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

@Component
public class GoogleBooksProvider implements MediaSearchProvider {

    static final String SOURCE = "GOOGLE_BOOKS";

    private final RestClient client;
    private final String apiKey;

    public GoogleBooksProvider(RestClient.Builder builder,
                               @Value("${app.google-books.base-url}") String baseUrl,
                               @Value("${app.google-books.api-key:}") String apiKey) {
        this.client = builder.baseUrl(baseUrl).build();
        this.apiKey = apiKey;
    }

    @Override
    public boolean supports(MediaType type) {
        return type == null || type == MediaType.BOOK;
    }

    @Override
    public List<SearchResult> search(String query, MediaType type) {
        JsonNode root = client.get()
                .uri(uri -> {
                    var b = uri.path("/volumes")
                            .queryParam("q", query)
                            .queryParam("maxResults", 20)
                            .queryParam("printType", "books");
                    if (!apiKey.isBlank()) {
                        b.queryParam("key", apiKey);
                    }
                    return b.build();
                })
                .retrieve()
                .body(JsonNode.class);

        List<SearchResult> results = new ArrayList<>();
        if (root == null) return results;

        for (JsonNode item : root.path("items")) {
            JsonNode info = item.path("volumeInfo");
            results.add(new SearchResult(
                    SOURCE,
                    item.path("id").asText(),
                    info.path("title").asText("Untitled"),
                    joinAuthors(info.path("authors")),
                    MediaType.BOOK,
                    TextUtils.https(info.path("imageLinks").path("thumbnail").asText(null)),
                    TextUtils.stripHtml(info.path("description").asText(null)),
                    TextUtils.intOrNull(info, "pageCount")
            ));
        }
        return results;
    }

    private static String joinAuthors(JsonNode authors) {
        if (!authors.isArray() || authors.isEmpty()) return null;
        List<String> names = new ArrayList<>();
        authors.forEach(a -> names.add(a.asText()));
        return String.join(", ", names);
    }
}