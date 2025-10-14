package com.libapp.backend.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/router")
public class RouterController {

    private static final Map<String, List<String>> ROLE_ACCESS = Map.of(
            "ROLE_ADMIN", List.of("/", "/acervo", "/emprestimos", "/atrasos", "/relatorios", "/perfil", "/configuracao"),
            "ROLE_USER", List.of("/", "/acervo", "/emprestimos", "/perfil", "/notificacoes", "/favoritos")
    );

    @GetMapping("/can-access")
    public ResponseEntity<Map<String, Boolean>> canAccess(@RequestParam String path, Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("allowed", false));
        }

        String role = authentication.getAuthorities().iterator().next().getAuthority();
        List<String> allowedPaths = ROLE_ACCESS.getOrDefault(role, List.of());
        boolean allowed = allowedPaths.stream().anyMatch(path::startsWith);

        return ResponseEntity.ok(Map.of("allowed", allowed));
    }

    // 👇 New endpoint
    @GetMapping("/allowed")
    public ResponseEntity<List<String>> getAllowedRoutes(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String role = authentication.getAuthorities().iterator().next().getAuthority();
        return ResponseEntity.ok(ROLE_ACCESS.getOrDefault(role, List.of()));
    }
}
