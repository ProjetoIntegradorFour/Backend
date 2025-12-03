package com.libapp.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.libapp.backend.dto.CatalogSummaryDTO;
import com.libapp.backend.service.CatalogService;

@RestController
@RequestMapping("/catalog")
@CrossOrigin
public class PublicCatalogController {

    private final CatalogService catalogService;

    public PublicCatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping
    public List<CatalogSummaryDTO> getPublicCatalog() {
        return catalogService.findAllCatalogSummaries();
    }
}
