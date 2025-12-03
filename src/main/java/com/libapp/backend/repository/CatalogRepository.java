package com.libapp.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.libapp.backend.dto.CatalogSummaryDTO;
import com.libapp.backend.entity.Catalog;

public interface CatalogRepository extends JpaRepository<Catalog, Long> {

    Optional<Catalog> findByIsbn(String isbn);

    boolean existsByIsbn(String isbn);

    List<Catalog> findByTitleContainingIgnoreCase(String title);

    List<Catalog> findByAuthorsContainingIgnoreCase(String author);

    void deleteByIsbn(String isbn);

    @Query("""
        SELECT new com.libapp.backend.dto.CatalogSummaryDTO(
            c.isbn,
            c.title,
            c.authors,
            c.coverUrl,
            (SELECT COUNT(cp) FROM Copy cp 
             WHERE cp.catalog.isbn = c.isbn 
             AND cp.status = 'AVAILABLE')
        )
        FROM Catalog c
        ORDER BY c.title
    """)
    List<CatalogSummaryDTO> findAllCatalogSummaries();
    
}
