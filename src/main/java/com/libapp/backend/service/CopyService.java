package com.libapp.backend.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.libapp.backend.entity.Copy;
import com.libapp.backend.entity.CopyStatus;
import com.libapp.backend.repository.CopyRepository;

@Service
@Transactional
public class CopyService {

    private static final Logger log = LoggerFactory.getLogger(CopyService.class);

    private final CopyRepository copyRepository;
    private final CatalogService catalogService;

    public CopyService(CopyRepository copyRepository, CatalogService catalogService) {
        this.copyRepository = copyRepository;
        this.catalogService = catalogService;
    }

    public List<Copy> findByIsbn(String isbn) {
        return copyRepository.findByCatalogIsbn(isbn);
    }

    public long countAvailableCopies(String isbn) {
        List<Copy> copies = copyRepository.findByCatalogIsbn(isbn);
        return copies.stream()
                .filter(copy -> copy.getStatus() == CopyStatus.AVAILABLE)
                .count();
    }

    public Copy save(Copy copy) {
        return copyRepository.save(copy);
    }

    public void delete(Long id) {
        copyRepository.deleteById(id);
    }
}
