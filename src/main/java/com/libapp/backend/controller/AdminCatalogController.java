package com.libapp.backend.controller;

import com.libapp.backend.entity.Catalog;
import com.libapp.backend.entity.Copy;
import com.libapp.backend.service.CatalogService;
import com.libapp.backend.service.CopyService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    // Fetch metadata from ISBN API and save
    @PostMapping("/fetch/{isbn}")
    public Catalog fetchAndInsert(@PathVariable String isbn) {
        return catalogService.fetchFromIsbnApi(isbn);
    }

    // CRUD
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

    // Copies
    @PostMapping("/{isbn}/copies")
    public Copy addCopy(@PathVariable String isbn, @RequestBody Copy copy) {
        Catalog catalog = catalogService.findByIsbn(isbn);
        copy.setCatalog(catalog);
        return copyService.save(copy);
    }

    @GetMapping("/{isbn}/copies")
    public List<Copy> listCopies(@PathVariable String isbn) {
        return copyService.findByIsbn(isbn);
    }
}
