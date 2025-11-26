package com.libapp.backend.controller;

import com.libapp.backend.entity.Catalog;
import com.libapp.backend.entity.Copy;
import com.libapp.backend.service.CatalogService;
import com.libapp.backend.service.CopyService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

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
        return catalogService.findAll().stream().map(c -> {
            List<Copy> copies = copyService.findByIsbn(c.getIsbn());
            long available = copies.stream()
                    .filter(copy -> copy.getStatus().isAvailable())
                    .count();

            return Map.of(
                    "isbn", c.getIsbn(),
                    "title", c.getTitle(),
                    "author", c.getAuthor(),
                    "cover", c.getCoverUrl(),
                    "availableCopies", available
            );
        }).collect(Collectors.toList());
    }
}
