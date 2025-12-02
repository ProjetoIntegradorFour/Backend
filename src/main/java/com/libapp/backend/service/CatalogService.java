package com.libapp.backend.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.libapp.backend.entity.Catalog;
import com.libapp.backend.repository.CatalogRepository;

@Service
@Transactional
public class CatalogService {

    private static final Logger log = LoggerFactory.getLogger(CatalogService.class);

    private final CatalogRepository catalogRepository;
    private final IsbnLookupService isbnLookupService;

    public CatalogService(CatalogRepository catalogRepository, IsbnLookupService isbnLookupService) {
        this.catalogRepository = catalogRepository;
        this.isbnLookupService = isbnLookupService;
    }

    public List<Catalog> findAll() {
        return catalogRepository.findAll();
    }

    public Optional<Catalog> findByIsbn(String isbn) {
        return catalogRepository.findByIsbn(isbn);
    }

    public Catalog save(Catalog catalog) {
        return catalogRepository.save(catalog);
    }

    public void delete(String isbn) {
        Optional<Catalog> catalog = catalogRepository.findByIsbn(isbn);
        catalog.ifPresent(catalogRepository::delete);
    }

    public Catalog fetchFromIsbnApi(String isbn) {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new RuntimeException("ISBN não pode ser vazio");
        }

        if (catalogRepository.existsByIsbn(isbn)) {
            throw new RuntimeException("Catálogo com ISBN " + isbn + " já existe");
        }

        Catalog metadata = isbnLookupService.fetchMetadata(isbn);
        return catalogRepository.save(metadata);
    }
}
