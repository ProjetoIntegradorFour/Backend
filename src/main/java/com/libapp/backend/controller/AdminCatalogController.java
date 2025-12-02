package com.libapp.backend.controller;

import java.util.List;

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

@RestController
@RequestMapping("/admin/catalog")
@CrossOrigin
public class AdminCatalogController {

    private final CatalogService catalogService;
    private final CopyService copyService;

    public AdminCatalogController(CatalogService catalogService, CopyService copyService) {
        this.catalogService = catalogService;
        this.copyService = copyService;
    }

    @PostMapping("/{isbn}/copies")
    public Copy addCopy(@PathVariable String isbn, @RequestBody Copy copy) {
        Catalog catalog = catalogService.findByIsbn(isbn)
                .orElseThrow(() -> new RuntimeException("Catalog not found for ISBN: " + isbn));
        copy.setCatalog(catalog);
        return copyService.save(copy);
    }

    @PostMapping
    public Catalog create(@RequestBody Catalog catalog) {
        return catalogService.save(catalog);
    }

    @PutMapping("/{isbn}")
    public Catalog update(@PathVariable String isbn, @RequestBody Catalog catalog) {
        catalog.setIsbn(isbn);
        return catalogService.save(catalog);
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
