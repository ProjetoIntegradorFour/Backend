package com.libapp.backend.service;

import com.libapp.backend.entity.Catalog;
import com.libapp.backend.repository.CatalogRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CatalogService {

    private final CatalogRepository catalogRepository;
    private final IsbnLookupService isbnLookupService;

    public CatalogService(CatalogRepository catalogRepository, IsbnLookupService isbnLookupService) {
        this.catalogRepository = catalogRepository;
        this.isbnLookupService = isbnLookupService;
    }

    public List<Catalog> findAll() {
        return catalogRepository.findAll();
    }

    public Catalog findByIsbn(String isbn) {
        return catalogRepository.findById(isbn).orElse(null);
    }

    public Catalog save(Catalog catalog) {
        return catalogRepository.save(catalog);
    }

    public void delete(String isbn) {
        catalogRepository.deleteById(isbn);
    }

    public Catalog fetchFromIsbnApi(String isbn) {
        Catalog metadata = isbnLookupService.fetchMetadata(isbn);
        return catalogRepository.save(metadata);
    }
}
