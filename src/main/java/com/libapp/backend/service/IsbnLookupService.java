package com.libapp.backend.service;

import com.libapp.backend.entity.Catalog;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

@Service
public class IsbnLookupService {

    private final RestTemplate rest = new RestTemplate();

    private static final String API_URL = "https://example.com/isbn/{isbn}";

    public Catalog fetchMetadata(String isbn) {
        ResponseEntity<ExternalBookResponse> response =
                rest.getForEntity(API_URL, ExternalBookResponse.class, isbn);

        ExternalBookResponse data = response.getBody();

        Catalog cat = new Catalog();
        cat.setIsbn(isbn);
        cat.setTitle(data.getTitle());
        cat.setAuthor(data.getAuthor());
        cat.setPublisher(data.getPublisher());
        cat.setLanguage(data.getLanguage());
        cat.setPublishYear(data.getYear());
        cat.setCoverUrl(data.getCover());

        return cat;
    }

    // Create a DTO matching your ISBN API
    public static class ExternalBookResponse {
        private String title;
        private String author;
        private String publisher;
        private String language;
        private Integer year;
        private String cover;

        // getters + setters
    }
}
