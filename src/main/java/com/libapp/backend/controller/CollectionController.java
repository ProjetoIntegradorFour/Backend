package com.libapp.backend.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.libapp.backend.repository.BookRepository;

@RestController
@RequestMapping("/api/collections")
public class CollectionController {

    private final BookRepository bookRepository;

    public CollectionController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping
    public Map<String, Object> getCollections(Pageable pageable) {
        var page = bookRepository.findAll(pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("content", page.getContent());
        response.put("totalPages", page.getTotalPages());
        response.put("totalElements", page.getTotalElements());
        response.put("size", page.getSize());
        response.put("number", page.getNumber());

        return response;
    }
}
