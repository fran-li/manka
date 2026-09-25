package pe.edu.utec.manka.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import pe.edu.utec.manka.dto.HistorialRequestDto;
import pe.edu.utec.manka.dto.HistorialResponseDto;
import pe.edu.utec.manka.service.HistorialService;

import java.util.List;

@RestController
@RequestMapping("/history")
public class HistorialController {
    private final HistorialService historialService;

    public HistorialController(HistorialService historialService) {
        this.historialService = historialService;
    }

    @PostMapping
    public ResponseEntity<HistorialResponseDto> create(@AuthenticationPrincipal Jwt jwt,
                                                        @Valid @RequestBody HistorialRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(historialService.create(jwt.getSubject(), request));
    }

    @GetMapping
    public List<HistorialResponseDto> getAll(@AuthenticationPrincipal Jwt jwt) {
        return historialService.getAll(jwt.getSubject());
    }
}
