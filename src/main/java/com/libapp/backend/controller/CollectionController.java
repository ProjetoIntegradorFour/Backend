package com.libapp.backend.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.libapp.backend.entity.Book;
import com.libapp.backend.repository.BookRepository;

@RestController
@RequestMapping("/api/collections")
public class CollectionController {
    
    private final BookRepository bookRepository;
    
    public CollectionController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }
    
    @GetMapping
    public Page<Book> getCollections(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }
}
