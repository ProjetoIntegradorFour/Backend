package com.libapp.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.libapp.backend.entity.Catalog;

public interface CatalogRepository extends JpaRepository<Catalog, Long> {

    Optional<Catalog> findByIsbn(String isbn);

    boolean existsByIsbn(String isbn);

    List<Catalog> findByTitleContainingIgnoreCase(String title);

    List<Catalog> findByAuthorsContainingIgnoreCase(String author);
}
