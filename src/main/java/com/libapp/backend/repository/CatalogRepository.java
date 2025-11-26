package com.libapp.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.libapp.backend.entity.Catalog;

public interface CatalogRepository extends JpaRepository<Catalog, Long> {
    
    Catalog findByIsbn(String isbn);
}
