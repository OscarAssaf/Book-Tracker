package com.example.booktracker.search;

import com.example.booktracker.domain.MediaType;
import com.example.booktracker.dto.SearchResult;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Anime, manga and manhwa via AniList's GraphQL API.
 * Manhwa are manga whose country of origin is South Korea (KR).
 */
@Component
public class AniListProvider implements MediaSearchProvider {

    static final String SOURCE = "ANILIST";

    private static final String QUERY = """
            query ($search: String, $type: MediaType, $perPage: Int) {
              Page(page: 1, perPage: $perPage) {
                media(search: $search, type: $type, sort: SEARCH_MATCH) {
                  id
                  type
                  countryOfOrigin
                  title { romaji english }
                  description(asHtml: false)
                  coverImage { large }
                  chapters
                  episodes
                  staff(perPage: 2) { nodes { name { full } } }
                }
              }
            }
            """;

    private final WebClient client;
    private final ObjectMapper mapper;

    public AniListProvider(WebClient.Builder builder,
                           @Value("${app.anilist.base-url}") String baseUrl) {
        this.client = builder.baseUrl(baseUrl).build();
        this.mapper = new ObjectMapper();
    }

    @Override
    public boolean supports(MediaType type) {
        return type != MediaType.BOOK;
    }

    @Override
    public List<SearchResult> search(String query, MediaType type) {
        List<String> aniTypes = type == null ? List.of("MANGA", "ANIME")
                : type == MediaType.ANIME ? List.of("ANIME")
                : List.of("MANGA");

        List<SearchResult> results = new ArrayList<>();
        for (String aniType : aniTypes) {
            Map<String, Object> variables = Map.of("search", query, "type", aniType, "perPage", 15);

            try {
                String response = client.post()
                        .bodyValue(Map.of("query", QUERY, "variables", variables))
                        .retrieve()
                        .bodyToMono(String.class)
                        .block();

                if (response == null) continue;

                JsonNode root = mapper.readTree(response);
                for (JsonNode media : root.path("data").path("Page").path("media")) {
                    MediaType mapped = mapType(media);
                    if (type != null && mapped != type) continue;
                    results.add(toResult(media, mapped));
                }
            } catch (Exception e) {
                // Log and continue
                continue;
            }
        }
        return results;
    }

    private static MediaType mapType(JsonNode media) {
        if ("ANIME".equals(media.path("type").asText())) return MediaType.ANIME;
        return "KR".equals(media.path("countryOfOrigin").asText()) ? MediaType.MANHWA : MediaType.MANGA;
    }

    private static SearchResult toResult(JsonNode m, MediaType type) {
        JsonNode title = m.path("title");
        String displayTitle = title.hasNonNull("english")
                ? title.get("english").asText()
                : title.path("romaji").asText("Untitled");

        List<String> staff = new ArrayList<>();
        m.path("staff").path("nodes").forEach(n -> staff.add(n.path("name").path("full").asText()));

        Integer units = type == MediaType.ANIME
                ? TextUtils.intOrNull(m, "episodes")
                : TextUtils.intOrNull(m, "chapters");

        return new SearchResult(
                SOURCE,
                m.path("id").asText(),
                displayTitle,
                staff.isEmpty() ? null : String.join(", ", staff),
                type,
                TextUtils.https(m.path("coverImage").path("large").asText(null)),
                TextUtils.stripHtml(m.path("description").asText(null)),
                units
        );
    }
}