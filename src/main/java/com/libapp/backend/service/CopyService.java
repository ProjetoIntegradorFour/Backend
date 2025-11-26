package com.libapp.backend.service;

import com.libapp.backend.entity.Copy;
import com.libapp.backend.repository.CopyRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CopyService {

    private final CopyRepository copyRepository;

    public CopyService(CopyRepository copyRepository) {
        this.copyRepository = copyRepository;
    }

    public List<Copy> findByIsbn(String isbn) {
        return copyRepository.findByCatalogIsbn(isbn);
    }

    public Copy save(Copy copy) {
        return copyRepository.save(copy);
    }

    public void delete(Long id) {
        copyRepository.deleteById(id);
    }
}
