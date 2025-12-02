package com.libapp.backend.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.libapp.backend.service.CatalogService;
import com.libapp.backend.service.CopyService;

@RestController
@RequestMapping("/catalog")
@CrossOrigin
public class PublicCatalogController {

    private final CatalogService catalogService;
    private final CopyService copyService;

    public PublicCatalogController(CatalogService catalogService, CopyService copyService) {
        this.catalogService = catalogService;
        this.copyService = copyService;
    }

    @GetMapping
    public List<Map<String, Object>> getPublicCatalog() {
        return catalogService.findAll().stream().map(catalog -> {
            long available = copyService.countAvailableCopies(catalog.getIsbn());

            Map<String, Object> map = new HashMap<>();
            map.put("isbn", catalog.getIsbn());
            map.put("title", catalog.getTitle());
            map.put("author", catalog.getAuthors());
            map.put("cover", catalog.getCoverUrl());
            map.put("availableCopies", available);

            return map;
        }).collect(Collectors.toList());
    }
}
