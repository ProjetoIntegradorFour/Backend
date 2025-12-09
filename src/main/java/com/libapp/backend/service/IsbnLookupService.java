package com.libapp.backend.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.libapp.backend.entity.Catalog;
import com.libapp.backend.exception.ResourceNotFoundException;

@Service
public class IsbnLookupService {

    private static final Logger log = LoggerFactory.getLogger(IsbnLookupService.class);

    private final RestTemplate restTemplate = new RestTemplate();

    public Catalog fetchMetadata(String isbn) {
        log.info("Fetching metadata for ISBN: {}", isbn);

        try {
            String url = "https://openlibrary.org/api/books?bibkeys=ISBN:" + isbn + "&jscmd=data&format=json";

            // The response is a Map<String, Object> where the key is "ISBN:XXXXXXXXXX"
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            if (response == null || response.isEmpty()) {
                throw new ResourceNotFoundException("Book metadata not found for ISBN in OpenLibrary: " + isbn);
            }

            String key = "ISBN:" + isbn;
            Map<String, Object> bookData = (Map<String, Object>) response.get(key);

            if (bookData == null) {
                throw new ResourceNotFoundException("Book metadata not found for ISBN in OpenLibrary: " + isbn);
            }

            return mapResponseToCatalog(isbn, bookData);

        } catch (HttpClientErrorException.NotFound e) {
            log.warn("ISBN {} not found in OpenLibrary", isbn);
            throw new ResourceNotFoundException("Book metadata not found for ISBN in OpenLibrary: " + isbn);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error fetching metadata for ISBN {}: {}", isbn, e.getMessage());
            throw new ResourceNotFoundException("External API error for ISBN: " + isbn + ". Message: " + e.getMessage());
        }
    }

    private Catalog mapResponseToCatalog(String isbn, Map<String, Object> data) {
        Catalog catalog = new Catalog();
        catalog.setIsbn(isbn);

        // Title
        String title = (String) data.get("title");
        catalog.setTitle(title != null ? title : "Título Desconhecido");

        // Authors
        catalog.setAuthors(extractAuthors(data));

        // Publisher
        List<String> publishers = (List<String>) data.get("publishers");
        catalog.setPublisher(publishers != null && !publishers.isEmpty()
                ? String.join(", ", publishers) : "Editora Desconhecida");

        // Published date
        String publishDate = (String) data.get("publish_date");
        catalog.setPublishedDate(publishDate != null ? publishDate : "Data Desconhecida");

        // Language detection for Portuguese
        List<Map<String, String>> languages = (List<Map<String, String>>) data.get("languages");
        if (languages != null && !languages.isEmpty()) {
            String langKey = languages.get(0).get("key");
            catalog.setLanguage(langKey.contains("por") ? "pt" : "en");
        } else {
            catalog.setLanguage("en");
        }

        // Cover - The URL pattern is reliable, regardless of API response structure.
        catalog.setCoverUrl("https://covers.openlibrary.org/b/isbn/" + isbn + "-L.jpg");

        // Description - Can be a String or a Map, need to handle both.
        Object descriptionObject = data.get("description");
        String description = null;
        if (descriptionObject instanceof String) {
            description = (String) descriptionObject;
        } else if (descriptionObject instanceof Map) {
            // Handle cases where description is nested (e.g., {"value": "..."})
            description = (String) ((Map<String, Object>) descriptionObject).get("value");
        }

        catalog.setDescription(description != null ? description : "Sem descrição disponível");

        catalog.setLastSyncedAt(LocalDateTime.now());

        return catalog;
    }

    private String extractAuthors(Map<String, Object> data) {
        try {
            List<Map<String, Object>> authors = (List<Map<String, Object>>) data.get("authors");
            if (authors != null && !authors.isEmpty()) {
                return authors.stream()
                        .map(a -> (String) a.get("name"))
                        .filter(Objects::nonNull)
                        .collect(Collectors.joining(", "));
            }
        } catch (Exception e) {
            log.debug("Could not parse authors", e);
        }
        return "Autor Desconhecido";
    }

    private Catalog createFallbackCatalog(String isbn) {
        Catalog catalog = new Catalog();
        catalog.setIsbn(isbn);
        catalog.setTitle("Livro não encontrado - ISBN: " + isbn);
        catalog.setAuthors("Desconhecido");
        catalog.setPublisher("Desconhecida");
        catalog.setLanguage("pt");
        catalog.setCoverUrl(null);
        catalog.setLastSyncedAt(LocalDateTime.now());
        catalog.setDescription("Falha ao buscar metadados do OpenLibrary. Título e dados inseridos manualmente.");
        return catalog;
    }
}
