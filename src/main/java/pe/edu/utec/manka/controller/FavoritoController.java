package pe.edu.utec.manka.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import pe.edu.utec.manka.dto.PlatoResponseDto;
import pe.edu.utec.manka.service.FavoritoService;

import java.util.List;

@RestController
@RequestMapping("/favorites")
public class FavoritoController {
    private final FavoritoService favoritoService;

    public FavoritoController(FavoritoService favoritoService) {
        this.favoritoService = favoritoService;
    }

    @PostMapping("/{dishId}")
    public ResponseEntity<Void> add(@AuthenticationPrincipal Jwt jwt, @PathVariable Long dishId) {
        favoritoService.add(jwt.getSubject(), dishId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{dishId}")
    public ResponseEntity<Void> remove(@AuthenticationPrincipal Jwt jwt, @PathVariable Long dishId) {
        favoritoService.remove(jwt.getSubject(), dishId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public List<PlatoResponseDto> getAll(@AuthenticationPrincipal Jwt jwt) {
        return favoritoService.getAll(jwt.getSubject());
    }
}
