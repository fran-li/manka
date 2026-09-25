package pe.edu.utec.manka.controller;

import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import pe.edu.utec.manka.dto.DespensaRequestDto;
import pe.edu.utec.manka.dto.IngredienteResponseDto;
import pe.edu.utec.manka.service.DespensaService;

import java.util.List;

@RestController
@RequestMapping("/pantry")
public class DespensaController {
    private final DespensaService despensaService;

    public DespensaController(DespensaService despensaService) {
        this.despensaService = despensaService;
    }

    @GetMapping
    public List<IngredienteResponseDto> get(@AuthenticationPrincipal Jwt jwt) {
        return despensaService.get(jwt.getSubject());
    }

    @PutMapping
    public List<IngredienteResponseDto> replace(@AuthenticationPrincipal Jwt jwt,
                                                 @Valid @RequestBody DespensaRequestDto request) {
        return despensaService.replace(jwt.getSubject(), request);
    }
}
