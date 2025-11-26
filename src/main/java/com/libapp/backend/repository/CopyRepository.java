package com.libapp.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.libapp.backend.entity.Copy;

public interface CopyRepository extends JpaRepository<Copy, Long> {
    
}
