package com.libapp.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.libapp.backend.entity.Catalog;
import com.libapp.backend.entity.Copy;
import com.libapp.backend.service.CatalogService;
import com.libapp.backend.service.CopyService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/admin/catalog")
@CrossOrigin
@PreAuthorize("hasRole('ADMIN')")
public class AdminCatalogController {

    private final CatalogService catalogService;
    private final CopyService copyService;

    public AdminCatalogController(CatalogService catalogService, CopyService copyService) {
        this.catalogService = catalogService;
        this.copyService = copyService;
    }

    @PostMapping("/fetch/{isbn}")
    public Catalog fetchAndInsert(@PathVariable String isbn) {
        return catalogService.fetchFromIsbnApi(isbn);
    }

    @PostMapping("/{isbn}/copies")
    public ResponseEntity<Copy> addCopy(
            @PathVariable String isbn,
            @Valid @RequestBody Copy copy) {

        Catalog catalog = catalogService.findByIsbn(isbn)
                .orElseThrow(() -> new RuntimeException("Catalog not found for ISBN: " + isbn));
        copy.setCatalog(catalog);
        Copy savedCopy = copyService.save(copy);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCopy);
    }

    @PostMapping
    public ResponseEntity<Catalog> create(@Valid @RequestBody Catalog catalog) {
        Catalog savedCatalog = catalogService.save(catalog);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCatalog);
    }

    @PutMapping("/{isbn}")
    public ResponseEntity<Catalog> update(
            @PathVariable String isbn,
            @Valid @RequestBody Catalog catalog) {

        catalog.setIsbn(isbn);
        Catalog updatedCatalog = catalogService.save(catalog);
        return ResponseEntity.ok(updatedCatalog);
    }

    @DeleteMapping("/{isbn}")
    public void delete(@PathVariable String isbn) {
        catalogService.delete(isbn);
    }

    @GetMapping("/{isbn}/copies")
    public List<Copy> listCopies(@PathVariable String isbn) {
        catalogService.findByIsbn(isbn)
                .orElseThrow(() -> new RuntimeException("Catalog not found for ISBN: " + isbn));

        return copyService.findByIsbn(isbn);
    }
}
