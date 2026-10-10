package com.example.booktracker.search;

import com.fasterxml.jackson.databind.JsonNode;

public final class TextUtils {

    private TextUtils() {}

    public static String stripHtml(String text) {
        if (text == null) return null;
        return text.replaceAll("<[^>]*>", " ").replaceAll("\\s+", " ").trim();
    }

    public static String https(String url) {
        if (url == null) return null;
        return url.startsWith("http://") ? "https://" + url.substring(7) : url;
    }

    public static Integer intOrNull(JsonNode node, String field) {
        return node.hasNonNull(field) ? node.get(field).asInt() : null;
    }
}